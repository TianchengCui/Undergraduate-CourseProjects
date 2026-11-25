library ieee;
use ieee.std_logic_1164.all;

entity tb_FourBitBiDirSftRgt is
end tb_FourBitBiDirSftRgt;

architecture tb of tb_FourBitBiDirSftRgt is

    component FourBitBiDirSftRgt
        port (
            CLK    : in std_logic;
            DataIn : in std_logic;
            R_Lbar : in std_logic;
            Q0     : buffer std_logic;
            Q1     : buffer std_logic;
            Q2     : buffer std_logic;
            Q3     : buffer std_logic
        );
    end component;

    signal CLK    : std_logic;
    signal DataIn : std_logic;
    signal R_Lbar : std_logic;
    signal Q0, Q1, Q2, Q3 : std_logic;

    constant TbPeriod : time := 200 ns;--Define the period of the test clock signal(can be changed)
    signal TbClock : std_logic := '0';
    signal TbSimEnded : std_logic := '0';--A signal that can control the simulation process

begin

    dut : FourBitBiDirSftRgt
    port map (
        CLK    => CLK,
        DataIn => DataIn,
        R_Lbar => R_Lbar,
        Q0     => Q0,
        Q1     => Q1,
        Q2     => Q2,
        Q3     => Q3
    );

    --Generate the clock signal
    TbClock <= not TbClock after TbPeriod/2 when TbSimEnded /= '1' else '0'; --Set the duty cycle of the clock to 50%
    CLK <= TbClock;
    
    --The simulation process
    stimuli : process
    begin
        wait for 0 ns; --Make the time flow immediately after the simulation starts

        -- Initialize the inputs
        DataIn <= '1';
        R_Lbar <= '1';
        wait for TbPeriod;--Make the condition last for a period

        --Change the input data
        DataIn <= '0';
        wait for TbPeriod;

        R_Lbar <= '0';--Change the direction
        DataIn <= '1';
        wait for TbPeriod;

        DataIn <= '0';
        wait for TbPeriod;

        --End the simulation
        TbSimEnded <= '1';
        wait; --Use wait to keep the simulation alive instead of being automatically closed
    end process;

end tb;