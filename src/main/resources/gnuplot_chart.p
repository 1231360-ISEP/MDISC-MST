set datafile separator ";"
plot './src/main/resources/out/us14/us14.csv' u 1:2 w lp title 'Execution time (ms)' smooth unique
set xlabel 'Graph size'
set ylabel 'Execution time (ms)'
set grid
set terminal png size 1600, 900
set output './src/main/resources/out/us14/chart.png'
replot
