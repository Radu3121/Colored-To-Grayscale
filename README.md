# Colored-To-Grayscale
A software used to transform colored images to grayscale images using the Average method. It's input is made of 2 image file paths, one to serve as the original and one to write the new image in. The processing is done in parallel, each thread modifies a specific portion of the image. The level parallelism is capped by the maximum number of useable threads of the host machine.