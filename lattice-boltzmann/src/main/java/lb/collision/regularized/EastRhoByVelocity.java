/*  Lattice Boltzmann sample, written in Java
 *
 *  Main author: Jean-Luc Falcone
 *  Co-author: Jonas Latt
 *  Copyright (C) 2006 University of Geneva
 *  Address: Jean-Luc Falcone, Rue General Dufour 24,
 *           1211 Geneva 4, Switzerland 
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public 
 *  License along with this program; if not, write to the Free 
 *  Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 *  Boston, MA  02110-1301, USA.
*/

package lb.collision.regularized;

import lb.collision.D2Q9RegularizedBoundary;

/** Computation of the density on eastern velocity boundaries */
public class EastRhoByVelocity implements RhoComputer {

    private final int[] freeLinks;
    public final double[] fNeq;

    public EastRhoByVelocity() {
	freeLinks = new int[] { 1, 2, 4, 5, 8 };
	fNeq = new double[9];
    }

    @Override
    public double computeRho(double[] f, D2Q9RegularizedBoundary collOp) {
	double[] u0 = collOp.u(f);
	return 1. / (1. + u0[0]) * (f[0] + f[2] + f[4] + 2 * (f[1] + f[5] + f[8]));
    }

}
