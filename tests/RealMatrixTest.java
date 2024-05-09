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

import merrimackutil.math.linear.RealMatrix;

public class RealMatrixTest {

    @Test
    public void testToString()
    {
        RealMatrix mat = RealMatrix.getIdentity(3);
        assertEquals("[ 1.000 0.000 0.000 ]\n[ 0.000 1.000 0.000 ]\n[ 0.000 0.000 1.000 ]", mat.toString());
    }

    @Test
    public void testSquareMatrixMultiply() 
    {
        RealMatrix res = new RealMatrix(3);
        res.setEntry(0, 0, 2.0);
        res.setEntry(1, 1, 2.0);
        res.setEntry(2, 2, 2.0);

        RealMatrix I3 = RealMatrix.getIdentity(3);

        RealMatrix prod = res.multiply(I3);
        assertEquals("Multiplication by identity.", res, prod);
    }

    @Test
    public void testScalarMultiply() 
    {
        RealMatrix res = new RealMatrix(3);
        res.setEntry(0, 0, 2.0);
        res.setEntry(1, 1, 2.0);
        res.setEntry(2, 2, 2.0);

        RealMatrix I3 = RealMatrix.getIdentity(3);
        RealMatrix ans = I3.scalarMultiply(2.0);

        assertEquals("Compute 2 * I_3", res, ans);
    }

    @Test
    public void testTranspose() 
    {
        RealMatrix rowVector = new RealMatrix(1, 3);
        RealMatrix colVector = new RealMatrix(3, 1);

        // Initialize the row and column vector vector.
        for (int i = 0; i < 3; i++)
        {
            colVector.setEntry(i, 0, (i + 1));
            rowVector.setEntry(0, i, (i + 1));
        }   

        RealMatrix rTranspose = rowVector.transpose();
        assertEquals("row transpose is same a column vector. ", colVector, rTranspose);
    }
}
