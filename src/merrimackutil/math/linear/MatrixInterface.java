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
 * This interface describes a matrix class.
 */
public interface MatrixInterface 
{
    /**
     * Gets the number of rows.
     * @return the number of rows.
     */
    public int getNumRows();

    /**
     * Gets the number of columns.
     * @return the number of columns.
     */
    public int getNumCols();
    
    /**
     * Add {@code matB} to this matrix.
     * @param matB the matrix to add.
     */
    public void add(MatrixInterface matB);

    /**
     * Left multiply this matrix by {@code matB}
     * @param matB the matrix to multiply by.
     */
    public void leftMultiply(MatrixInterface matB);

    /**
     * Right multiply this matrix by {@code matB}.
     * @param matB the matrix to multiply by.
     */
    public void rightMultiply(MatrixInterface matB);

    /**
     * Compute the transpose of this matrix.
     */
    public void transpose();

    /**
     * Invert this matrix.
     */
    public void invert();    
}
