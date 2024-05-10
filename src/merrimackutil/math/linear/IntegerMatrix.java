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

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Represents an integer valued matrix. The entries are restricted to 
 * 32-bit numbers (ints). An integer matrix is an immutable 
 * object.
 */
public class IntegerMatrix
{
    private int numRows;        // The number of rows in the matrix.
    private int numCols;        // The number of columns in the matrix.
    private int[][] entries; // The entries of the matrix.

    /**
     * Build a real matrix with {@code numRows} rows and {@code numCols} columns.
     * @param numRows the number of rows in the matrix.
     * @param numCols the number of columsn in the matrix.
     */
    public IntegerMatrix(int numRows, int numCols)
    {
        this.numRows = numRows;
        this.numCols = numCols;
        entries = new int[numRows][numCols];
    }

    /**
     * Cosntructs a new square matrix.
     * @param dim the dimension of the matrix.
     */
    public IntegerMatrix(int dim)
    {
        this.numRows = dim;
        this.numCols = dim;

        entries = new int[dim][dim];
    }

    /**
     * This is a copy constructor that constructs an identical 
     * copy of matrix {@code cpy}.
     * @param cpy the matrix to copy.
     */
    public IntegerMatrix(IntegerMatrix cpy)
    {
        this.numRows = cpy.getNumRows();
        this.numCols = cpy.getNumCols();

        entries = new int[this.numRows][this.numCols];
        for (int i = 0; i < numRows; i++)
            for(int j = 0; j < numCols; j++)
                entries[i][j] = cpy.entries[i][j];
    }

    /**
     * This method gets the identity matrix I_n.
     * @param n the dimension of the square matrix.
     * @return the RealMatrix I_n.
     */
    public static IntegerMatrix getIdentity(int n)
    {
        IntegerMatrix mat = new IntegerMatrix(n, n);

        for (int i = 0; i < n; i++)
            mat.setEntry(i, i, 1);
        return mat;
    }

    /**
     * This method builds a random real matrix with values drawn 
     * from the interval [base, limit)
     * @param base the minimum possible value an entry can be.
     * @param limit the limit for the entry.
     * @param numRows the number of rows in the matrix.
     * @param numCols the number of columns in the matrix.
     * @return a random matrix.
     */
    public static IntegerMatrix getRandomMatrix(int base, int limit, int numRows, int numCols)
    {
        IntegerMatrix mat = new IntegerMatrix(numRows, numCols);
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.setEntry(i, j, rand.nextInt(base, limit));
        return mat;
    }

    /**
     * Gives a real form of this matrix.
     * @return the real form of this matrix.
     */
    public RealMatrix toRealMatrix()
    {
        RealMatrix res = new RealMatrix(numRows, numCols);

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                res.setEntry(i, j, (double)entries[i][j]);
        return res;
    }

    /**
     * Set the value of an entry in the matrix.
     * @param row the row the entry is in.
     * @param col the column the entry is in.
     * @param val the value stored in the given location.
     * @throws IllegalArgumentException if the location is not valid.
     */
    public void setEntry(int row, int col, int val) throws IllegalArgumentException
    {
        if (row < numRows && row >= 0 && col < numCols && col >= 0)
            entries[row][col] = val;
        else
            throw new IllegalArgumentException("Entry location (" + row + ", " + col + ") is invalid.");
    }

    /**
     * Get the value of an entry in the matrix.
     * @param row the row of the entry.
     * @param col the column of the entry.
     * @return the entry at location ({@code row}, {@code column}).
     * @throws IllegalArgumentException if the location is not valid.
     */
    public int getEntry(int row, int col) throws IllegalArgumentException
    {
        if (row < numRows && row >= 0 && col < numCols && col >= 0)
            return entries[row][col];
        else
            throw new IllegalArgumentException("Entry location (" + row + ", " + col + ") is invalid.");
    }

    /**
     * Adds this matrix and {@code matB}.
     * @param matB the matrix to add to this matrix.
     * @return a new matrix that is the sum of this matrix and {@code matB}.
     */
    public IntegerMatrix add(IntegerMatrix matB) 
    {
        IntegerMatrix mat = new IntegerMatrix(numRows, numCols);
   
        if (matB.getNumCols() != numCols || matB.getNumRows() != numRows)
            throw new UnsupportedOperationException("mismatched dimensions.");

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.entries[i][j] = entries[i][j] + matB.entries[i][j];

        return mat;
    }

    /**
     * Subtract {@code matB} from this matrix.
     * @param matB the matrix to subtract from this matrix.
     * @return a new matrix that is the difference of this matrix and {@code matB}.
     */
    public IntegerMatrix subtract(IntegerMatrix matB) 
    {
        IntegerMatrix mat = new IntegerMatrix(numRows, numCols);
   
        if (matB.getNumCols() != numCols || matB.getNumRows() != numRows)
            throw new UnsupportedOperationException("mismatched dimensions.");

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.entries[i][j] = entries[i][j] - matB.entries[i][j];

        return mat;
    }

    /**
     * Multiply this matrix by the scalar {@code scalar}.
     * @param scalar the scalar to multiply each entry by.
     * @return a matrix that results from scalar multiplying this matrix.
     */
    public IntegerMatrix scalarMultiply(int scalar)
    {
        IntegerMatrix mat = new IntegerMatrix(numRows, numCols);

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.entries[i][j] = scalar * entries[i][j];
        
        return mat;
    }

    /**
     * Left multiply this matrix by {@code matB}.
     * @param matB the matrix to left multiply by.
     * @return the result of left multiplying this by {@code matB}.
     * @throws IllegalArgumentException if there is a dimension mismatch.
     */
    public IntegerMatrix multiply(IntegerMatrix matB) throws IllegalArgumentException
    {
        IntegerMatrix res = new IntegerMatrix(this.numRows, matB.getNumCols());

        // Make sure we can multiply the two matrices.
        if (this.numCols != matB.getNumRows())
            throw new IllegalArgumentException("Matrix mulitplication not possible, mismathced columns and rows.");

        // We will do the silly O(n^3) method as Strassen's matrix multiply is not that memory 
        // efficient in general. 
        for (int i = 0; i < this.numRows; i++)
            for (int j = 0; j < matB.getNumCols(); j++)
                for (int k = 0; k < this.numCols; k++)
                    res.setEntry(i, j, res.getEntry(i, j) + entries[i][k] * matB.getEntry(k, j));

        return res;
    }

    /**
     * Compute the transpose of this matrix.
     * @return the transpose of this matrix.
     */
    public IntegerMatrix transpose() 
    {
        IntegerMatrix mat = new IntegerMatrix(numCols, numRows);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.entries[j][i] = entries[i][j];
        return mat;
    }

    /**
     * Return the inverse of this matrix.
     * @return the inverse of this matrix.
     */
    public IntegerMatrix invert() 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'invert'");
    }

    /**
     * Get the number of rows in the matrix.
     * @return the number of rows.
     */
    
    public int getNumRows() 
    {
        return numRows;
    }

    /**
     * Get the number of columns in the matrix.
     * @return the number columns.
     */
    
    public int getNumCols() 
    {
        return numCols;
    }

   
    /**
     * Compares this matrix and the passed in matrix and returns {@code true} 
     * if they are equal and {@code false} otherwise.
     * @param obj the matrix to compare against.
     * @return {@code true} if the matrices are equal and {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        IntegerMatrix other = (IntegerMatrix) obj;
        if (numRows != other.numRows)
            return false;
        if (numCols != other.numCols)
            return false;
        if (!Arrays.deepEquals(entries, other.entries))
            return false;
        return true;
    }

    /**
     * Construct a string representation of the matrix.
     * @return the String representation of the matrix.
     */
    @Override
    public String toString() 
    {
        String res = "";
        for (int i = 0; i < numRows; i++)
        {
            String row = "[ ";
            for (int j = 0; j < numCols; j++)
                row += String.format("%d", entries[i][j]) + " ";
            row += "]\n";
            res += row;
        }
        return res.stripTrailing();
    }
}
