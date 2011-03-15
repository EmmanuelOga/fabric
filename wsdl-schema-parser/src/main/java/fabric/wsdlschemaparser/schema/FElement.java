/**
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the
 * name of the University of Luebeck nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 *
 */
package fabric.wsdlschemaparser.schema;

/**
 * @author Marco Wegner
 */
public class FElement extends FSchemaObject {

	// --------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------

	// TODO
	public static final int UNBOUNDED = 20;

	public static final FElement BYTE_ARRAY = new FElement("", new FByte());

	static {
		BYTE_ARRAY.setMaxOccursUnbounded();
	}

	// --------------------------------------------------------------------
	// Member variables
	// --------------------------------------------------------------------

	/**
	 * The type this element is referencing.
	 */
	private FSchemaType schemaType;

	// --------------------------------------------------------------------

	/**
	 * The minimum occurrence of this element.
	 */
	private int minOccurs;

	// --------------------------------------------------------------------

	/**
	 * The maximum occurrence of this element.
	 */
	private int maxOccurs;

	// --------------------------------------------------------------------

	/**
	 * Stores whether this element is a top-level one or not.
	 */
	private boolean isTopLevel;

	private boolean isReference;

	// --------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------

	/**
	 * @param name
	 * @param ftype
	 */
	public FElement(String name, FSchemaType ftype) {
		super(name);
		setSchemaType(ftype);
		setMinOccurs(1);
		setMaxOccurs(1);
		setTopLevel(false);
		setReference(false);
	}

	// --------------------------------------------------------------------
	// Methods
	// --------------------------------------------------------------------

	/**
	 * @param schemaType
	 */
	private void setSchemaType(FSchemaType schemaType) {
		if (schemaType == null)
			throw new IllegalArgumentException("The referenced type can't be null!");
		this.schemaType = schemaType;
	}

	// --------------------------------------------------------------------

	/**
	 * @return
	 */
	public FSchemaType getSchemaType() {
		return schemaType;
	}

	// --------------------------------------------------------------------

	/**
	 * @param minOccurs
	 */
	public void setMinOccurs(int minOccurs) {
		if (minOccurs < 0)
			throw new IllegalArgumentException("minOccurs can't be negative");
		this.minOccurs = minOccurs;
	}

	// --------------------------------------------------------------------

	/**
	 * @return
	 */
	public int getMinOccurs() {
		return minOccurs;
	}

	// --------------------------------------------------------------------

	/**
	 * @param maxOccurs
	 */
	public void setMaxOccurs(int maxOccurs) {
		if (maxOccurs < 0)
			throw new IllegalArgumentException("maxOccurs can't be negative");
		this.maxOccurs = maxOccurs;
	}

	// --------------------------------------------------------------------

	/**
	 * @return
	 */
	public int getMaxOccurs() {
		return maxOccurs;
	}

	public FElement getReferencedTopLevelElement() {
		if (isReference()) {
			return getFSchema().getTopLevelObjectList().getTopLevelElement(getName());
		}
		throw new RuntimeException("Not a reference " + getName());
	}

	// --------------------------------------------------------------------

	/**
	 *
	 */
	public void setMaxOccursUnbounded() {
		setMaxOccurs(UNBOUNDED);
	}

	// --------------------------------------------------------------------

	/**
	 * @param isTopLevel
	 */
	public void setTopLevel(boolean isTopLevel) {
		this.isTopLevel = isTopLevel;
	}

	// --------------------------------------------------------------------

	/**
	 * @return
	 */
	public boolean isTopLevel() {
		return isTopLevel;
	}

	public boolean isReference() {
		return isReference;
	}

	public void setReference(boolean isReference) {
		this.isReference = isReference;
	}

	/* (non-Javadoc)
	 * @see fabric.schema.types.FSchemaObject#equals(fabric.schema.types.FSchemaObject)
	 * @author Daniel Bimschas
	 */
	@Override
	public boolean equals(FSchemaObject other) {
		if (this == other)
			return true;

		if (!(other instanceof FElement))
			return false;

		if (!isTopLevel && (minOccurs != ((FElement) other).minOccurs || maxOccurs != ((FElement) other).maxOccurs))
			return false;

		return schemaType.equals(((FElement) other).schemaType);
	}
}
