package dynamicform;

import com.lowagie.text.DocumentException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public final class Form {
    private final NumberFormat JPY = NumberFormat.getCurrencyInstance(Locale.JAPAN);
    private Stamper stamper;

    public Form(String title) {
        this.title = title;
    }

    private String title = "";
    private String issuedTo = "";
    private String meta1 = "";
    private String meta2 = "";
    private String meta3 = "";
    private String meta1Value = "";
    private String meta2Value = "";
    private String meta3Value = "";
    String issuedBy = "";
    String shortDescription = "";
    String note = "";

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public void setMeta1(String meta1) {
        this.meta1 = meta1;
    }

    public void setMeta2(String meta2) {
        this.meta2 = meta2;
    }

    public void setMeta3(String meta3) {
        this.meta3 = meta3;
    }

    public void setMeta1Value(String meta1Value) {
        this.meta1Value = meta1Value;
    }

    public void setMeta2Value(String meta2Value) {
        this.meta2Value = meta2Value;
    }

    public void setMeta3Value(String meta3Value) {
        this.meta3Value = meta3Value;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setNote(String note) {
        this.note = note;
    }

    List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        if (items.size() == 10) {
            throw new IllegalStateException("number of items cannot exceed 10");
        }
        items.add(item);
    }

    final int 消費税率 = 8;

    public void saveAs(OutputStream outputStream, String templateForm, String... excludes) throws IOException, DocumentException {
        stamper = new Stamper(Form.class.getResourceAsStream("/" + templateForm), outputStream);
        stamper.set("title", title);
        stamper.set("issued-to", issuedTo);
        stamper.set("issued-by", issuedBy);
        stamper.set("short-description", shortDescription);
        stamper.set("meta1", meta1);
        stamper.set("meta2", meta2);
        stamper.set("meta3", meta3);
        stamper.set("meta1-value", meta1Value);
        stamper.set("meta2-value", meta2Value);
        stamper.set("meta3-value", meta3Value);
        int totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            stamper.set("item" + (i + 1), item.getName());
            stamper.set("quantity" + (i + 1), item.getQuantity());
            int price = item.getUnitPrice() * item.getQuantity();
            stamper.set("price" + (i + 1), JPY.format(item.getUnitPrice() * item.getQuantity()));
            stamper.set("unitprice" + (i + 1), JPY.format(item.getUnitPrice()));
            totalPrice += price;
        }
        stamper.set("tax", JPY.format(totalPrice * 消費税率 / 100));
        stamper.set("total-price", JPY.format(totalPrice * (100 + 消費税率) / 100));
        stamper.set("note", note);
        stamper.close(excludes);
    }
    public void saveAs(String saveTo, String templateForm, String... excludes) throws IOException, DocumentException {
        saveAs(new FileOutputStream(saveTo), templateForm,excludes);
    }

}
