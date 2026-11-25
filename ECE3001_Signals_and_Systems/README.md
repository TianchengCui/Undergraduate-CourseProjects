# ECE3001 — Speaker Identification

Signals and Systems project comparing ResNet-18 and batch-normalized VGG-11 with time-domain and short-time Fourier transform (STFT) features. The [experiment report](project-report.pdf) records the results for all four configurations.

- [Part I — Data collection](part-1-data-collection.pdf) and [audio segmentation script](split_audio.py)
- [Part II — Experiment and analysis](part-2-experiment-and-analysis.pdf) and [training notebook](speaker_identification.ipynb)
- [Evaluation examples](results/)

Install the notebook dependencies with `python -m pip install -r requirements.txt`. Open the notebook from this directory and provide the original course dataset as `dataset2025.zip` or as `dataset2025/train` and `dataset2025/test`. The notebook writes checkpoints to `checkpoints/` and evaluates the checkpoint matching the selected model and feature settings.

To split a WAV file that you have permission to use, create a `segments/` directory and run `python split_audio.py --in_audio recording.wav --out_dir segments --duration 10`.

Full training requires access to the course dataset. The archive includes result snapshots. The recording, course dataset, and trained checkpoints are external.
