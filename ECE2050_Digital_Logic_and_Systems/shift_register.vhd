library ieee;
use ieee.std_logic_1164.ALL;

--Define the external I/Os of the D Flip Flop
entity DFlipFlop is
    port(
        D   : in std_logic;
        CLK : in std_logic;
        Q   : buffer std_logic
    );
end DFlipFlop;

-- Define the function of D Flip Flops
architecture Behavioral of DFlipFlop is
    --Initialize the Q value to be 0
    --Use the variable  Q_internal to prevent the variable Q from being rewritten
    signal Q_internal : std_logic := '0';
begin
    Q <= Q_internal;
    process (CLK)
    begin
        --D is passed to Q at the rising edge
        if rising_edge(CLK) then
            Q_internal <= D;
        end if;
    end process;
end Behavioral;

--Restate the logic used for each entity
library ieee;
use ieee.std_logic_1164.ALL;

--Define the external I/Os of the bidirectional shift register
entity FourBitBiDirSftRgt is
--R_Lbar means data shifts right when this signal is high and left when it is low
--DataIn is the input data sequence
port (CLK,DataIn,R_Lbar:in std_logic;
    Q0,Q1,Q2,Q3:buffer std_logic);
end FourBitBiDirSftRgt;

--Define the function of the bidirectional shift register
architecture Behavioral of FourBitBiDirSftRgt is

component DFlipFlop is
    port(D,CLK:in std_logic; 
    Q:buffer std_logic);
end component DFlipFlop;

--Define the inputs for the flip flops in sequence
signal D0,D1,D2,D3:std_logic;

--Use sturctural design to implement the bidirectional shift register
begin
    --Define the shift process using AND logic 
    D0<=(DataIn and R_Lbar) or (Q1 and not R_Lbar);
    D1<=(Q0 and R_Lbar) or (Q2 and not R_Lbar);
    D2<=(Q1 and R_Lbar) or (Q3 and not R_Lbar);
    D3<=(Q2 and R_Lbar) or (DataIn and not R_Lbar);
    --Define the port map of every flip flop
    D_FF0:DFlipFlop port map (D=>D0, CLK=>CLK,Q=>Q0);
    D_FF1:DFlipFlop port map (D=>D1, CLK=>CLK,Q=>Q1);
    D_FF2:DFlipFlop port map (D=>D2, CLK=>CLK,Q=>Q2);
    D_FF3:DFlipFlop port map (D=>D3, CLK=>CLK,Q=>Q3);
end Behavioral;
