/**
 * Copyright (c) 2010, Dennis Pfisterer, Marco Wegner, Institute of Telematics, University of Luebeck
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
/**
 * 
 */
package fabric.module.dot;

import java.util.Properties;

import de.uniluebeck.sourcegen.Workspace;
import fabric.module.api.FabricModule;
import fabric.module.api.FabricSchemaTreeItemHandler;

/**
 * Fabric module used for creating Graphviz dot visualisations of the Schema
 * object tree.
 * 
 * @author Marco Wegner
 */
public class FabricDotGraphModule implements FabricModule {

    /**
     * Option key used for the Dot graph output file.
     */
    private static final String KEY_DOT_OUTFILE = "dot.outfile";

    /**
     * Constructs a new module.
     */
    public FabricDotGraphModule( ) {
        super( );
    }

    @Override
    public String getName( ) {
        return "dot";
    }

    @Override
    public String getDescription( ) {
        return String.format("Creates a Graphviz DOT file. Valid options are '%s'.",
                KEY_DOT_OUTFILE);
    }

    @Override
    public Properties getDefaultProperties( ) {
        final Properties p = new Properties( );
        p.put(KEY_DOT_OUTFILE, "dotfile.dot");
        return p;
    }

    @Override
    public FabricSchemaTreeItemHandler getHandler(Workspace workspace, Properties properties)
            throws Exception {
        return new FabricDotGraphHandler(workspace, properties);
    }
}
