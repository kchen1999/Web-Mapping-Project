package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    double estimates[];
    double numOfExperiments;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if(N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        estimates = new double[T];
        numOfExperiments = T;
        for(int i = 0; i < T; i++) {
            Percolation perf = pf.make(N);
            while(true) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                if(!perf.isOpen(row, col)) {
                    perf.open(row, col);
                    if(perf.percolates()) {
                        estimates[i] = (double) perf.numberOfOpenSites() / (double) (N * N);
                        break;
                    }
                }

            }
        }
    }
    public double mean() {
        return StdStats.mean(estimates);
    }
    public double stddev() {
        return StdStats.stddev(estimates);
    }

    public double confidenceLow() {
        return (mean() - (1.96 * stddev() / Math.sqrt(numOfExperiments)));
    }

    public double confidenceHigh() {
        return (mean() + (1.96 * stddev() / Math.sqrt(numOfExperiments)));
    }

    public static void main(String[] args) {
        PercolationFactory perf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(20, 5000, perf);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());
        System.out.println("Confidence interval between " + ps.confidenceLow() + " " + ps.confidenceHigh());
    }
}
