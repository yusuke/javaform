package dynamicform;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import java.io.*;
import java.util.Set;

/*
 * Copyright (C) 2013 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class Stamper {
    private final PdfStamper stamper;
    private final AcroFields fields;
    private boolean closed = false;

    public Stamper(String inputPath, String outputPath) throws IOException, DocumentException {
        this(new FileInputStream(inputPath), new FileOutputStream(outputPath));
    }

    public Stamper(InputStream inputPath, OutputStream outputPath) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputPath);
        stamper = new PdfStamper(reader, outputPath);
        reader.close();
        fields = stamper.getAcroFields();
    }

    public void set(String key, String value) throws IOException, DocumentException {
        fields.setField(key, value);
    }

    public void set(String key, int value) throws IOException, DocumentException {
        fields.setField(key, String.valueOf(value));
    }

    public synchronized void close(String... fieldsLeaveUnflatten) throws IOException, DocumentException {
        if(closed){
            throw new IllegalStateException("already closed");
        }
        closed = true;
        Set keySet = fields.getFields().keySet();
        if (fieldsLeaveUnflatten != null) {
            for (String exclude : fieldsLeaveUnflatten) {
                keySet.remove(exclude);
            }
        }
        for (Object keyobj : keySet) {
            String key = (String) keyobj;
            stamper.partialFormFlattening(key);
        }
        stamper.setFormFlattening(true);
        stamper.setFreeTextFlattening(true);
        stamper.close();
    }
}
