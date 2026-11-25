# EE123 Final Project Proposal

Jerry Cui and Miaodi Duan

## Abstract

We have chosen Deconvolution / Deblurring as the topic of our final project which is about image deblurring using some basic ideas from DSP. The main problem we want to look at is how to recover a clear image from a blurred one, where the blur can be modeled as a convolution with a point spread function, for example, caused by motion or defocus. We initially plan to implement a few widely accepted methods in the frequency domain, such as inverse filtering and Wiener filtering, and use Python tools like NumPy, SciPy, and OpenCV to run the algorithms and find out if they could generate reasonable results.

Our general approach is to transform the image into the Fourier domain, perform the deconvolution there, and then convert it back. We are also interested in how these methods behave when there is noise, since in practice images are usually not clean, and different methods may respond very differently. For example, some methods may amplify noise a lot while others are more stable, so we want to compare them under different conditions. In addition, we want to try a simple idea of adjusting parameters based on the image itself (controlling other variables at the same time), instead of using fixed values, to see if that makes any difference.

More details are still left to discuss, so this is just an immature thought. Do expect further advice & investigation!
