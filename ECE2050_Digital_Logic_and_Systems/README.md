# ECE2050 — Four-Bit Bidirectional Shift Register

Digital Logic and Systems project implementing a four-bit register by wiring four D flip-flops. The `R_Lbar` input selects the shift direction and `DataIn` supplies the bit inserted at the corresponding end.

- [VHDL implementation](shift_register.vhd) and [behavioral testbench](tb_shift_register.vhd)
- [Simulation waveform](simulation-waveform.png) and [Vivado waveform configuration](tb_FourBitBiDirSftRgt_behav.wcfg)
- [Project specification](project-specification.pdf)

In Vivado, add `shift_register.vhd` as a design source and `tb_shift_register.vhd` as a simulation source. Set the simulation top to `tb_FourBitBiDirSftRgt` and run behavioral simulation to 1 µs.
