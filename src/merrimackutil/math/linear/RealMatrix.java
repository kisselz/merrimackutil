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
 * Represents a real valued matrix. The entries are restricted to 
 * 64-bit real numbers (Doubles). A real matrix is an immutable 
 * object.
 */
public class RealMatrix
{
    private int numRows;        // The number of rows in the matrix.
    private int numCols;        // The number of columns in the matrix.
    private double[][] entries; // The entries of the matrix.

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
     * Cosntructs a new square matrix.
     * @param dim the dimension of the matrix.
     */
    public RealMatrix(int dim)
    {
        this.numRows = dim;
        this.numCols = dim;

        entries = new double[dim][dim];
    }

    /**
     * This is a copy constructor that constructs an identical 
     * copy of matrix {@code cpy}.
     * @param cpy the matrix to copy.
     */
    public RealMatrix(RealMatrix cpy)
    {
        this.numRows = cpy.getNumRows();
        this.numCols = cpy.getNumCols();

        entries = new double[this.numRows][this.numCols];
        for (int i = 0; i < numRows; i++)
            for(int j = 0; j < numCols; j++)
                entries[i][j] = cpy.entries[i][j];
    }

    /**
     * This method gets the identity matrix I_n.
     * @param n the dimension of the square matrix.
     * @return the RealMatrix I_n.
     */
    public static RealMatrix getIdentity(int n)
    {
        RealMatrix mat = new RealMatrix(n, n);

        for (int i = 0; i < n; i++)
            mat.setEntry(i, i, 1.0);
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
    public static RealMatrix getRandomMatrix(double base, double limit, int numRows, int numCols)
    {
        RealMatrix mat = new RealMatrix(numRows, numCols);
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.setEntry(i, j, rand.nextDouble(base, limit));
        return mat;
    }

    /**
     * Gives an integer form of this matrix.
     * @return the integer form of this matrix.
     */
    public IntegerMatrix toIntegerMatrix()
    {
        IntegerMatrix res = new IntegerMatrix(numRows, numCols);

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                res.setEntry(i, j, (int)entries[i][j]);
        return res;
    }

    /**
     * Determines if the vector is zero or not.
     * @return {@code true} if the vector is zero. 
     */
    public boolean isZero()
    {
        for (int i = 0; i < numRows; i ++)
            for (int j = 0; j < numCols; j++)
                if (entries[i][j] != 0.0)
                    return false;
        return true;
    }

    /**
     * Determine if this vector is a row matrix.
     * 
     * @return true if the vector is a row vector.
     */
    public boolean isRowVector() {
        return numRows == 1;
    }

    /**
     * Determines if this matrix is a col vector.
     * 
     * @return true if this matrix is a column vector.
     */
    public boolean isColVector() {
        return numCols == 1;
    }

    /**
     * Determine if this matrix is a vector.
     * 
     * @return true if the matrix is a vector, false otherwise.
     */
    public boolean isVector() {
        return isRowVector() || isColVector();
    }

    /**
     * Compute the dot (or inner) product of two vectors.
     * 
     * @param mat the matrix to compute the inner product with.
     * @return the inner product.
     * @throws IllegalArgumentException when an inner product can not be taken.
     */
    public double innerProd(RealMatrix mat) throws IllegalArgumentException 
    {
        double res = 0;

        if (!mat.isVector() || !isVector())
            throw new IllegalArgumentException("Two vectors needed to take an inner product.");

        // One row and one column vector.
        if (isRowVector() && mat.isColVector())
            return multiply(mat).getEntry(0, 0);

        else if (isColVector() && mat.isRowVector())
            return mat.multiply(this).getEntry(0, 0);

        else if (isRowVector() && mat.isRowVector()) // two row vectors.
        {
            if (numRows != mat.getNumRows())
                throw new IllegalArgumentException("Dimension mismatch.");

            for (int i = 0; i < numCols; i++)
                res += getEntry(0, i) * mat.getEntry(0, i);

        } 
        else // two column vectors.
        {
            if (numCols != mat.getNumCols())
                throw new IllegalArgumentException("Dimension mismatch");

            for (int i = 0; i < numRows; i++)
                res += getEntry(i, 0) * mat.getEntry(i, 0);
        }
        return res;
    }

    /**
     * Gets the named row vector from the matrix.
     * 
     * @param rowId the row of the matrix to get.
     * @return the row vector.
     * @throws IllegalArgumentException if the row is invalid.
     */
    public RealMatrix getRowVector(int rowId) throws IllegalArgumentException 
    {
        RealMatrix rowVector;

        if (rowId > numRows || rowId < 0)
            throw new IllegalArgumentException("rowId out of range.");

        rowVector = new RealMatrix(1, getNumCols());
        for (int i = 0; i < numCols; i++)
            rowVector.setEntry(0, i, getEntry(rowId, i));
        return rowVector;
    }

    /**
     * Gets the specified column vector from the matrix.
     * 
     * @param colId the identifer of the column.
     * @return the column vector.
     * @throws IllegalArgumentException if the column does not exist.
     */
    public RealMatrix getColVector(int colId) throws IllegalArgumentException 
    {
        RealMatrix colVector;

        if (colId > numCols || colId < 0)
            throw new IllegalArgumentException("rowId out of range.");

        colVector = new RealMatrix(getNumRows(), 1);
        for (int i = 0; i < numRows; i++)
            colVector.setEntry(i, 0, getEntry(i, colId));
        return colVector;
    }


    /**
     * Computes the euclidean norm of the vector.
     * 
     * @return the Euclidean norm of the vector.
     * @throws UnsupportedOperationException if the matrix is not a vector.
     */
    public double euclideanNorm() throws UnsupportedOperationException 
    {
        if (!this.isVector())
            throw new UnsupportedOperationException("Can't take norm of a matrix.");

        return Math.sqrt((double) this.innerProd(this));
    }

    /**
     * Computes the inifity norm of the vector.
     * 
     * @return the infinity norm.
     * @throws UnsupportedOperationException if the matrix is not a vector.
     */
    public double infinityNorm() throws UnsupportedOperationException 
    {
        double max = -1;

        if (!this.isVector())
            throw new UnsupportedOperationException("Can't take norm of a matrix.");

        if (this.isRowVector()) {
            for (int i = 0; i < this.getNumCols(); i++)
                if (this.getEntry(0, i) > max)
                    max = this.getEntry(0, i);
        } 
        else 
        {
            for (int i = 0; i < this.getNumRows(); i++)
                if (this.getEntry(i, 0) > max)
                    max = this.getEntry(i, 0);
        }

        return max;
    }

    /**
     * Computes the Manhattan norm of a vector.
     * 
     * @return the Manhattan norm.
     * @throws UnsupportedOperationException if the matrix is not a vector.
     */
    public double manhattanNorm() throws UnsupportedOperationException 
    {
        double dist = 0;

        if (!this.isVector())
            throw new UnsupportedOperationException("Can't take norm of a matrix");

        if (this.isRowVector())
            for (int i = 0; i < this.getNumCols(); i++)
                dist += this.getEntry(0, i);
        else
            for (int j = 0; j < this.getNumRows(); j++)
                dist += this.getEntry(j, 0);
        return dist;
    }

    /**
     * Set the value of an entry in the matrix.
     * @param row the row the entry is in.
     * @param col the column the entry is in.
     * @param val the value stored in the given location.
     * @throws IllegalArgumentException if the location is not valid.
     */
    public void setEntry(int row, int col, double val) throws IllegalArgumentException
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
    public double getEntry(int row, int col) throws IllegalArgumentException
    {
        if (row < numRows && row >= 0 && col < numCols && col >= 0)
            return entries[row][col];
        else
            throw new IllegalArgumentException("Entry location (" + row + ", " + col + ") is invalid.");
    }

    /**
     * Adds this matrix and {@code matB}.
     * @param matB the matrix to add to this matrix.
     * @return a new mathrix that is the sum of this matrix and {@code matB}.
     */
    public RealMatrix add(RealMatrix matB) 
    {
        RealMatrix mat = new RealMatrix(numRows, numCols);
   
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
    public RealMatrix subtract(RealMatrix matB) 
    {
        RealMatrix mat = new RealMatrix(numRows, numCols);
   
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
    public RealMatrix scalarMultiply(double scalar)
    {
        RealMatrix mat = new RealMatrix(numRows, numCols);

        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.entries[i][j] = scalar * entries[i][j];
        
        return mat;
    }

    /**
     * Right multiply this matrix by {@code matB}.
     * @param matB the matrix to left multiply by.
     * @return the result of left multiplying this by {@code matB}.
     * @throws IllegalArgumentException if there is a dimension mismatch.
     */
    public RealMatrix multiply(RealMatrix matB) throws IllegalArgumentException
    {
        RealMatrix res = new RealMatrix(this.numRows, matB.getNumCols());

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
    public RealMatrix transpose() 
    {
        RealMatrix mat = new RealMatrix(numCols, numRows);
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++)
                mat.entries[j][i] = entries[i][j];
        return mat;
    }

    /**
     * Return the inverse of this matrix.
     * @return the inverse of this matrix.
     */
    public RealMatrix invert() 
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

        RealMatrix other = (RealMatrix) obj;
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
                row += String.format("%.3f", entries[i][j]) + " ";
            row += "]\n";
            res += row;
        }
        return res.stripTrailing();
    }
}
