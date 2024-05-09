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
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import merrimackutil.math.linear.IntegerMatrix;

public class IntegerMatrixTest {

    @Test
    public void testToString()
    {
        IntegerMatrix mat = IntegerMatrix.getIdentity(3);
        assertEquals("[ 1 0 0 ]\n[ 0 1 0 ]\n[ 0 0 1 ]", mat.toString());
    }

    @Test
    public void testSquareMatrixMultiply() 
    {
        IntegerMatrix res = new IntegerMatrix(3);
        res.setEntry(0, 0, 2);
        res.setEntry(1, 1, 2);
        res.setEntry(2, 2, 2);

        IntegerMatrix I3 = IntegerMatrix.getIdentity(3);

        IntegerMatrix prod = res.multiply(I3);
        assertEquals("Multiplication by identity.", res, prod);
    }

    @Test
    public void testScalarMultiply() 
    {
        IntegerMatrix res = new IntegerMatrix(3);
        res.setEntry(0, 0, 2);
        res.setEntry(1, 1, 2);
        res.setEntry(2, 2, 2);

        IntegerMatrix I3 = IntegerMatrix.getIdentity(3);
        IntegerMatrix ans = I3.scalarMultiply(2);

        assertEquals("Compute 2 * I_3", res, ans);
    }

    @Test
    public void testTranspose() 
    {
        IntegerMatrix rowVector = new IntegerMatrix(1, 3);
        IntegerMatrix colVector = new IntegerMatrix(3, 1);

        // Initialize the row and column vector vector.
        for (int i = 0; i < 3; i++)
        {
            colVector.setEntry(i, 0, (i + 1));
            rowVector.setEntry(0, i, (i + 1));
        }   

        IntegerMatrix rTranspose = rowVector.transpose();
        assertEquals("row transpose is same a column vector. ", colVector, rTranspose);
    }
}
