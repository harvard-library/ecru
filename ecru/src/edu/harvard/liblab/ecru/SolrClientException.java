/****************************************************************************** 
 * Copyright 2010 Harvard University Library
 * 
 * This file is part of Batch Builder
 * 
 * Batch Builder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Batch Builder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Batch Builder.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************/

package edu.harvard.liblab.ecru;

/************************************************
 * Representation of an Exception thrown by bb
 * @author spmcewen
 ************************************************/
public class SolrClientException extends java.lang.Exception {
		
	private static final long serialVersionUID = -3862549961046427180L;
	public SolrClientException() {
		super();
	}
    public SolrClientException(String message) {
        super(message);
    }
    public SolrClientException(String message, Exception e) {
        super(message,e);
    }
}
