/**
 * Copyright (c) 2010, Institute of Telematics (Dennis Pfisterer, Marco Wegner, Dennis Boldt, Sascha Seidel, Joss Widderich), University of Luebeck
 *
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
package de.uniluebeck.sourcegen.c;

import java.util.List;

import de.uniluebeck.sourcegen.exceptions.CppDuplicateException;


public interface CppConstructor extends CppLangElem {

	class CppConstructorFactory {

		private static CppConstructorFactory instance;

		private CppConstructorFactory() { /* not to be invoked */
		}

		static CppConstructorFactory getInstance() {
			if (instance == null)
				instance = new CppConstructorFactory();
			return instance;
		}

		public CppConstructor create(CppVar... vars) throws CppDuplicateException {
			return new CppConstructorImpl(vars);
		}
	}

	public static final CppConstructorFactory factory = CppConstructorFactory.getInstance();

	public CppConstructor add(CppVar... create) throws CppDuplicateException;
	public CppConstructor add(String... pExtendeds) throws CppDuplicateException;
	public CppConstructor appendCode(String string);
	public String getSignature();
	public CppConstructor setComment(CComment comment);

	/**
	 * This method is only used by the CppClass
	 *
	 * @param clazz
	 * @return
	 */
	public CppConstructor setClass(CppClass clazz);

	public CppConstructor setInititalVars(List<CppVar> init);

	public CppConstructor add(CppConstructor... cons) throws CppDuplicateException;
}
