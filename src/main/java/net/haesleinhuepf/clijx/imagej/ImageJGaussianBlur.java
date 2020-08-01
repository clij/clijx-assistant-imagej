package net.haesleinhuepf.clijx.imagej;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents;
import org.scijava.plugin.Plugin;

/**
 * Demo plugin for integrating ImageJ based algorithms into CLIJ workflows.
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_imageJGaussianBlur")
public class ImageJGaussianBlur extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasLicense, HasAuthor
{

    public ImageJGaussianBlur() {
        super();
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number sigma_x, Number sigma_y, Number sigma_z";
    }

    @Override
    public boolean executeCL() {
        boolean result = imageJGaussianBlur(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]), asFloat(args[4]));
        return result;
    }

    public static boolean imageJGaussianBlur(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer output, Float sigma_x, Float sigma_y, Float sigma_z) {
        // get the image from GPU memory
        ImagePlus imp = clij2.pull(input1);

        // do the classical ImageJ operation, differently, depending on 2D or 3D
        if (input1.getDepth() > 1) { // 3D
            IJ.run(imp, "Gaussian Blur 3D...", "x=" + sigma_x + " y=" + sigma_y + " z=" + sigma_z);
        } else { // 2D
            IJ.run(imp, "Gaussian Blur...", "sigma=" + sigma_x);
        }

        // push result back to the GPU
        ClearCLBuffer result = clij2.push(imp);
        clij2.copy(result, output);
        result.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Apply ImageJs Gaussian Blur to an image.\n\nIn case of processing a 2D image, only signa_x is used.\n\nNote: This operation runs on the CPU.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Filter";
    }

    @Override
    public String getAuthorName() {
        return "Put your name here.";
    }

    @Override
    public String getLicense() {
        return "Public domain";
    }
}
