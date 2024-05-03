set datafile separator ";"
plot './us14.csv' u 1:2 w lp title 'Execution time (ms)' smooth unique
set xlabel 'Graph size'
set ylabel 'Execution time (ms)'
set grid
set terminal png size 1600, 900
set output './grafico.png'
replot
