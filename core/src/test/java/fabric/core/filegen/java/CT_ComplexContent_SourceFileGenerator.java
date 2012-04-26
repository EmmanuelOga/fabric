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
package fabric.core.filegen.java;

import de.uniluebeck.sourcegen.java.JClass;
import fabric.module.typegen.AttributeContainer;
import fabric.module.typegen.java.AnnotationMapper;
import fabric.module.typegen.java.JavaClassGenerationStrategy;

import java.util.Properties;

/**
 * SourceFileGenerator for complexType_complexContent.xsd
 */
public class CT_ComplexContent_SourceFileGenerator extends JSourceFileGenerator {

    /**
     * Constructor
     */
    public CT_ComplexContent_SourceFileGenerator(Properties properties) {
        super(properties);
    }

    /**
        * Generates the JComplexType objects corresponding to the test XSD.
        */
    @Override
    void generateClasses() throws Exception {

        /*
         * CarType
         */
        JavaClassGenerationStrategy strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass carType = ((JClass) AttributeContainer.newBuilder()
            .setName("CarType")
            .addElement("int", "HorsePower")
            .addElement("String", "LicenseNumber")
            .addElement("javax.xml.datatype.XMLGregorianCalendar", "ProductionYear")
            .build()
            .asClassObject(strategy));
        types.put(carType, strategy.getRequiredDependencies());

        /**
         * CarExtendedType
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        JClass extendedCarType = ((JClass) AttributeContainer.newBuilder()
            .setName("CarExtendedType")
            .addElement("java.math.BigDecimal", "Milage")
            .addElement("int", "Gears")
            .build()
            .asClassObject(strategy));
        extendedCarType.setExtends(carType);
        types.put(extendedCarType, strategy.getRequiredDependencies());

        /*
         * Root
         */
        strategy = new JavaClassGenerationStrategy(new AnnotationMapper(xmlFramework));
        types.put((JClass) AttributeContainer.newBuilder()
            .setName(rootName)
            .addElement("CarExtendedType", "ExtendedCar")
            .build()
            .asClassObject(strategy), strategy.getRequiredDependencies());
    }
}
