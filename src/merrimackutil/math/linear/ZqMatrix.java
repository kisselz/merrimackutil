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
 * Represents a matrix where elements are from the ring Z/qZ.
 */
public class ZqMatrix
{
    private IntegerMatrix matrix;       // The matrix.
    private int q;                      // The modulus.

    /**
     * Build a real matrix with {@code numRows} rows and {@code numCols} columns.
     * @param numRows the number of rows in the matrix.
     * @param numCols the number of columsn in the matrix.
     * @param q the modulus for the ring.
     */
    public ZqMatrix(int numRows, int numCols, int q)
    {
        this.matrix = new IntegerMatrix(numRows, numCols);
        this.q = q;
    }

    /**
     * Cosntructs a new square matrix.
     * @param dim the dimension of the matrix.
     * @param q the modulus for the ring.
     */
    public ZqMatrix(int dim, int q)
    {
        this(dim, dim, q);
    }

    /**
     * Construct a ZqMatrix from an integer matrix and a modulus q.
     * @param mat the matrix to map into Z/qZ with the same dimensions.
     * @param q the modulus of the ring.
     */
    public ZqMatrix(IntegerMatrix mat, int q)
    {
        this.matrix = new IntegerMatrix(mat);
        this.q = q;

        // Place each element into the ring Z/qZ.
        for (int i = 0; i < this.matrix.getNumRows(); i++)
            for (int j = 0; j < this.matrix.getNumCols(); j++)
            {
                int val = this.matrix.getEntry(i, j);

                while(val < 0)
                    val += q;
                this.matrix.setEntry(i, j, val % q);
            }
    }

    /**
     * This is a copy constructor that constructs an identical 
     * copy of matrix {@code cpy}.
     * @param cpy the matrix to copy.
     */
    public ZqMatrix(ZqMatrix cpy)
    {
        this.q = cpy.getQ();
        this.matrix = new IntegerMatrix(cpy.matrix);
    }

    /**
     * Get the modulus of the ring Z/qZ of the entries in 
     * the matrix.
     * @return the modulus q.
     */
    public int getQ() {
        return q;
    }

    /**
     * Determines if the vector is zero or not.
     * @return {@code true} if the vector is zero. 
     */
    public boolean isZero()
    {
        return matrix.isZero();
    }

    /**
     * Determine if this vector is a row matrix.
     * 
     * @return true if the vector is a row vector.
     */
    public boolean isRowVector() 
    {
        return matrix.isRowVector();
    }

    /**
     * Determines if this matrix is a col vector.
     * 
     * @return true if this matrix is a column vector.
     */
    public boolean isColVector() 
    {
        return matrix.isColVector();
    }

    /**
     * Determine if this matrix is a vector.
     * 
     * @return true if the matrix is a vector, false otherwise.
     */
    public boolean isVector() 
    {
        return isRowVector() || isColVector();
    }

    /**
     * Compute the dot (or inner) product of two vectors.
     * 
     * @param mat the matrix to compute the inner product with.
     * @return the inner product.
     * @throws IllegalArgumentException when an inner product can not be taken.
     */
    public int innerProd(ZqMatrix mat) throws IllegalArgumentException 
    {
        if (mat.q != this.q)
            throw new IllegalArgumentException("Mismatched modulus.");

        return matrix.innerProd(mat.matrix) % q;    
    }

    /**
     * Gets the named row vector from the matrix.
     * 
     * @param rowId the row of the matrix to get.
     * @return the row vector.
     * @throws IllegalArgumentException if the row is invalid.
     */
    public ZqMatrix getRowVector(int rowId) throws IllegalArgumentException 
    {
        return new ZqMatrix(matrix.getRowVector(rowId), q);
    }

    /**
     * Gets the specified column vector from the matrix.
     * 
     * @param colId the identifer of the column.
     * @return the column vector.
     * @throws IllegalArgumentException if the column does not exist.
     */
    public ZqMatrix getColVector(int colId) throws IllegalArgumentException 
    {
       return new ZqMatrix(matrix.getColVector(colId), q);
    }

    /**
     * Computes the euclidean norm of the vector.
     * @return the Euclidean norm of the vector.
     * @throws UnsupportedOperationException if the matrix is not a vector.
     */
    public double euclideanNorm() throws UnsupportedOperationException
    {
        return matrix.euclideanNorm();
    }

    /**
     * Computes the inifity norm of the vector.
     * @return the infinity norm.
     * @throws UnsupportedOperationException if the matrix is not a vector.
     */
    public int infinityNorm() throws UnsupportedOperationException
    {
       return matrix.infinityNorm();
    }

    /**
     * Computes the Manhattan norm of a vector.
     * @return the Manhattan norm.
     * @throws UnsupportedOperationException if the matrix is not a vector.
     */
    public int manhattanNorm() throws UnsupportedOperationException
    {
        return matrix.manhattanNorm();
    }

    /**
     * This method gets the identity matrix I_n.
     * @param n the dimension of the square matrix.
     * @return the RealMatrix I_n.
     */
    public static ZqMatrix getIdentity(int n)
    {
        ZqMatrix mat = new ZqMatrix(n, n);

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
     * @param q the modulus of the ring.
     * @return a random matrix.
     */
    public static ZqMatrix getRandomMatrix(int base, int limit, int numRows, int numCols, int q)
    {
       return new ZqMatrix(IntegerMatrix.getRandomMatrix(base, limit, numRows, numCols), q);
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
        // Handle the case when we have negative entries.
        while (val < 0)
            val += q;

        if (row < matrix.getNumRows() && row >= 0 && col < matrix.getNumCols() && col >= 0)
            matrix.setEntry(row, col, val % q);
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
        return matrix.getEntry(row, col);
    }

    /**
     * Adds this matrix and {@code matB}.
     * @param matB the matrix to add to this matrix.
     * @return a new matrix that is the sum of this matrix and {@code matB}.
     * @throws IllegalArgumentException if the matrices do not have the same modulus.
     */
    public ZqMatrix add(ZqMatrix matB) throws IllegalArgumentException 
    {
        if (this.q != matB.getQ())
            throw new IllegalArgumentException("Matrices don't have matching modulus.");
        return new ZqMatrix(this.matrix.add(matB.matrix), q);
    }

    /**
     * Subtract {@code matB} from this matrix.
     * 
     * @param matB the matrix to subtract from this matrix.
     * @return a new matrix that is the difference of this matrix and {@code matB}.
     * @throws IllegalArgumentException if the matrices do not have the same modulus.
     */
    public ZqMatrix subtract(ZqMatrix matB) throws IllegalArgumentException
    {
        if (this.q != matB.getQ())
            throw new IllegalArgumentException("Matrices don't have matching modulus.");

        IntegerMatrix negMat = matB.matrix.scalarMultiply(-1);
        return this.add(new ZqMatrix(negMat, q));
    }

    /**
     * Multiply this matrix by the scalar {@code scalar}.
     * @param scalar the scalar to multiply each entry by.
     * @return a matrix that results from scalar multiplying this matrix.
     */
    public ZqMatrix scalarMultiply(int scalar)
    {
        IntegerMatrix prod;

        while (scalar < 0)
            scalar += q;
        prod = this.matrix.scalarMultiply(scalar);
        return new ZqMatrix(prod, q);
    }

    /**
     * Right multiply this matrix by {@code matB}.
     * @param matB the matrix to left multiply by.
     * @return the result of left multiplying this by {@code matB}.
     * @throws IllegalArgumentException if there is a dimension mismatch.
     */
    public ZqMatrix multiply(ZqMatrix matB) throws IllegalArgumentException
    {
        if (this.q != matB.getQ())
            throw new IllegalArgumentException("Matrices don't have matching modulus.");

        IntegerMatrix prod = this.matrix.multiply(matB.matrix);
        return new ZqMatrix(prod, q);
    }

    /**
     * Compute the Hadamard Product (element-wise product) of two matrices.
     * 
     * @param mat the matrix to compute the product of.
     * @return the hadamard product matrix which has the same dimensions as matrix
     *         {@code mat}.
     * @throws IllegalArgumentException if this matrix and {@code mat} don't
     *                                  have the same dimensions.
     */
    public ZqMatrix hadamardMultiply(ZqMatrix mat) throws IllegalArgumentException 
    {
        if (this.q != mat.getQ())
            throw new IllegalArgumentException("Matrices don't have matching modulus.");

        IntegerMatrix prod = this.matrix.hadamardMultiply(mat.matrix);
        return new ZqMatrix(prod, q);
    }

    /**
     * Compute the transpose of this matrix.
     * @return the transpose of this matrix.
     */
    public ZqMatrix transpose() 
    {
        IntegerMatrix tran = this.matrix.transpose();
        return new ZqMatrix(tran, q);
    }

    /**
     * Return the inverse of this matrix.
     * @return the inverse of this matrix.
     */
    public ZqMatrix invert() 
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
        return this.matrix.getNumRows();
    }

    /**
     * Get the number of columns in the matrix.
     * @return the number columns.
     */
    
    public int getNumCols() 
    {
        return this.matrix.getNumCols();
    }


    /**
     * Augment the columns of {@code this} with {@code mat} restuling in a new matrix.
     * @param mat the matrix to agument with (right matrix).
     * @return An augmented form of the matrix.
     * @throws IllegalArgumentException if the number of rows in the two matrices don't match.
     */
    public ZqMatrix augmentCols(ZqMatrix mat) throws IllegalArgumentException
    {
        IntegerMatrix res = this.matrix.augmentCols(mat.matrix);

        if (this.q != mat.getQ())
            throw new IllegalArgumentException("Moduli must match in column augmentation.");

        return new ZqMatrix(res, mat.getQ());
    }

    /**
     * Augment the rows of {@code this} with {@code mat} restuling in a new matrix.
     * @param mat the matrix to agument with (lower matrix).
     * @return An augmented form of the matrix.
     * @throws IllegalArgumentException if the number of rows in the two matrices don't match.
     */
    public ZqMatrix augmentRows(ZqMatrix mat) throws IllegalArgumentException
    {
        IntegerMatrix res = this.matrix.augmentRows(mat.matrix);

        if (this.q != mat.getQ())
            throw new IllegalArgumentException("Moduli must match in row augmentation.");

        return new ZqMatrix(res, this.q);
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

        ZqMatrix other = (ZqMatrix) obj;
        if (this.q != other.q) 
            return false;
        return this.matrix.equals(other.matrix);
    }

    /**
     * Construct a string representation of the matrix.
     * @return the String representation of the matrix.
     */
    @Override
    public String toString() 
    {
        return this.matrix.toString();
    }
}
