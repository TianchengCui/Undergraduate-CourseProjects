final=eval(input('Enter the final account value:'))#get the final account value
rate=eval(input('Enter the annual interest rate:'))#get the annual interest rate
year=eval(input('Enter the number of years:'))#get the number of years
initial_value=final/(1+rate*10**-2)**year#calculate the initial value
print(initial_value)
