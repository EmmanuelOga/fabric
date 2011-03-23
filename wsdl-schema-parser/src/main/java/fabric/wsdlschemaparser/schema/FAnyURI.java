/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 *
 */
package fabric.wsdlschemaparser.schema;

import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.SchemaType;



/**
 * This class represents the Fabric variation of a normalized string,
 * as specified by xs:anyURI in XML Schema.
 *
 * @author Richard Mietz
 */
public class FAnyURI extends FString {

	public static final int MAX_LENGTH = 255;
	
	/**
	 *
	 */
	public FAnyURI( ) {
		this(null);
	}

	// --------------------------------------------------------------------

	/**
	 * Creates a normalized string having a type name.
	 * 
	 * @param typeName This normalized string's name.
	 */
	public FAnyURI(String typeName) {
		super(typeName);
		initialize( );
	}

	// --------------------------------------------------------------------

	/**
	 * Initialises the normalized string. Mostly here the initial restrictions are being
	 * set.
	 */
	private void initialize( ) {
		addRestriction(SchemaType.FACET_MIN_LENGTH, 0);
		addRestriction(SchemaType.FACET_MAX_LENGTH, MAX_LENGTH);
	}

	// --------------------------------------------------------------------
    // Methods
    // --------------------------------------------------------------------
	
	/**
	 * @see fabric.wsdlschemaparser.schema.FSchemaType#getValidFacets()
	 */
	@Override
	public List<Integer> getValidFacets( ) {
		return Arrays.asList(new Integer[] {
                SchemaType.FACET_LENGTH,
                SchemaType.FACET_MIN_LENGTH,
                SchemaType.FACET_MAX_LENGTH,
                SchemaType.FACET_ENUMERATION
        });
	}
}
