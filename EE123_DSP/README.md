# EE123 — Frequency-Domain Image Deblurring

UC Berkeley Spring 2026 DSP final project by Jerry Cui and Miaodi Duan. We compare inverse filtering, fixed-parameter Wiener filtering, spectral adaptive Wiener filtering, and a median absolute deviation (MAD) approach to choosing the Wiener parameter.

- [Project proposal](project-proposal.md), [final report](project-report.pdf) and [presentation](final-presentation.pdf)
- [Experiment code](deblurring_experiments.py), [environment dependencies](requirements.txt), and [input images](images/)
- [Per-case results](results/metrics_v2_full.csv), [summary](results/summary_by_method.csv), and [representative comparisons](results/)

Run the 144-condition experiment from this directory with Python 3:

```sh
python -m pip install -r requirements.txt
python deblurring_experiments.py --input_dir images --output_dir generated-results
```

The final report records 27.34 dB PSNR and 0.567 SSIM on average for MAD-adaptive Wiener filtering. The experiment uses six images from the [Kodak Lossless True Color Image Suite](https://www.r0k.us/graphics/kodak/) with motion blur, defocus blur, and four noise levels. See the [course website](https://sites.google.com/berkeley.edu/ee123-sp26) for course information.
