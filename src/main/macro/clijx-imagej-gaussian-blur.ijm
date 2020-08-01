
open("C:/structure/data/blobs.tif");
run("CLIJ2 Macro Extensions", "cl_device=");

// median
image1 = getTitle();
Ext.CLIJ2_push(image1);

radius_x = 2.0;
radius_y = 2.0;
Ext.CLIJ2_median2DBox(image1, image2, radius_x, radius_y);

// ImageJ gaussian blur
sigma_x = 2.0;
sigma_y = 2.0;
sigma_z = 2.0;
Ext.CLIJx_imageJGaussianBlur(image2, image3, sigma_x, sigma_y, sigma_z);
Ext.CLIJ_pull(image3);
