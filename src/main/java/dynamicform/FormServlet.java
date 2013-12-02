package dynamicform;

import com.lowagie.text.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Copyright 2013Yusuke Yamamoto
 * <p>
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class FormServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Form form = new Form(req.getParameter("title"));
        form.setMeta1("請求日");
        form.setMeta1Value(JapaneseDate.now().format(DateTimeFormatter.ofPattern("平成y年M月d日", Locale.JAPANESE)));
        form.setIssuedBy(req.getParameter("issuedBy"));
        form.setIssuedTo(req.getParameter("issuedTo"));
        form.setShortDescription(req.getParameter("shortDescription"));
        for (int i = 0; i < 10; i++) {
            if (req.getParameter("item" + i) != null) {
                form.addItem(new Item(
                        req.getParameter("item" + i),
                        Integer.parseInt(req.getParameter("item" + i + "quantity")),
                        Integer.parseInt(req.getParameter("item" + i + "price"))));
            }
        }
        form.setNote(req.getParameter("note"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
        try {
            form.saveAs(baos, "form.pdf", new String[]{});
        } catch (DocumentException e) {
            new ServletException(e);
        }
        res.setContentType("application/pdf");
        res.setBufferSize(baos.size());
        ServletOutputStream outputStream = res.getOutputStream();
        outputStream.write(baos.toByteArray());
    }
}
