package net.haesleinhuepf.clijx.imagej;

import ij.ImagePlus;
import ij.plugin.filter.Binary;
import ij.plugin.filter.EDM;
import ij.plugin.filter.MyBinary;
import ij.process.FloodFiller;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Demo plugin for integrating ImageJ based algorithms into CLIJ workflows.
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_imageJFillHoles")
public class ImageJFillHoles extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput, HasLicense, HasAuthor
{

    public ImageJFillHoles() {
        super();
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = imageJFillHoles(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
        return result;
    }

    public static boolean imageJFillHoles(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer output) {

        if (input1.getDimension() == 3 && input1.getDepth() > 1) {
            System.out.println("Warning: CLIJx_imageJWatershed is limited to 2D images only.");
        }

        // get the image from GPU memory
        ImageProcessor ip = clij2.pullBinary(input1).getProcessor();

        // process it using ImageJ
        MyBinary.fill(ip, 255, 0);

        // push result back to the GPU
        ClearCLBuffer result = clij2.push(new ImagePlus("bla", ip));
        clij2.equalConstant(result, output, 255);
        result.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Apply ImageJs Watershed algorithm to a binary image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public String getCategories() {
        return "Binary,Filter";
    }

    @Override
    public String getInputType() {
        return "Binary Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
    }

    @Override
    public String getLicense() {
        return "Public domain";
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase based on work by G. Landini";
    }
}
