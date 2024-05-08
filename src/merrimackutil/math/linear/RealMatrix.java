/*
 *   Copyright (C) 2024  Zachary A. Kissel
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package merrimackutil.math.linear;

/**
 * Represents a real valued matrix. The entries are restricted to 
 * 64-bit real numbers (Doubles).
 */
public class RealMatrix implements MatrixInterface
{
    private int numRows;
    private int numCols;
    private double[][] entries;

    /**
     * Build a real matrix with {@code numRows} rows and {@code numCols} columns.
     * @param numRows the number of rows in the matrix.
     * @param numCols the number of columsn in the matrix.
     */
    public RealMatrix(int numRows, int numCols)
    {
        this.numRows = numRows;
        this.numCols = numCols;
        entries = new double[numRows][numCols];
    }

    /**
     * Makes a copy of the the current matrix.
     * @return a copy of the matrix.
     */
    public RealMatrix copy()
    {
        RealMatrix rv = new RealMatrix(numRows, numCols);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                rv.entries[i][j] = entries[i][j];
        return rv;
    }

    /**
     * Add {@code matB} to this matrix.
     */
    @Override
    public void add(MatrixInterface matB) 
    {
        RealMatrix mat;
        if (!(matB instanceof RealMatrix))
            throw new UnsupportedOperationException("matrix must be a RealMatrix.");
        mat = (RealMatrix)matB;

        if (mat.getNumCols() != numCols || mat.getNumRows() != numRows)
            throw new UnsupportedOperationException("mismatched dimensions.");

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                entries[i][j] += mat.entries[i][j];
    }

    /**
     * Multiply this matrix by the scalar {@code scalar}.
     * @param scalar the scalar to multiply each entry by.
     */
    public void scalarMultiply(double scalar)
    {
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                entries[i][j] *= scalar;
    }

    @Override
    public void leftMultiply(MatrixInterface matB) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leftMultiply'");
    }

    @Override
    public void rightMultiply(MatrixInterface matB) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rightMultiply'");
    }

    @Override
    public void transpose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transpose'");
    }

    @Override
    public void invert() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'invert'");
    }

    /**
     * Get the number of rows in the matrix.
     * @return the number of rows.
     */
    @Override
    public int getNumRows() 
    {
        return numRows;
    }

    /**
     * Get the number of columns in the matrix.
     * @return the number columns.
     */
    @Override
    public int getNumCols() 
    {
        return numCols;
    }
    
}
