package dynamicform;

import com.lowagie.text.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public final class SofujoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);

            Stamper stamper = new Stamper(Form.class.getResourceAsStream("/" + "sofujo.pdf"), baos);
            String today = new SimpleDateFormat("yyyy年M月d日").format(new Date());
            stamper.set("title", req.getParameter("title"));
            stamper.set("meta1-value", today);
            stamper.set("issued-to", req.getParameter("issuedTo"));
            stamper.set("note1", req.getParameter("note1"));
            stamper.set("note2", req.getParameter("note2"));
            stamper.set("issued-by", req.getParameter("issuedBy"));

            stamper.close("note2");

            res.setContentType("application/pdf");
            res.setBufferSize(baos.size());
            ServletOutputStream outputStream = res.getOutputStream();
            outputStream.write(baos.toByteArray());

            baos.close();

        } catch (DocumentException e) {
            new ServletException(e);
        }
    }
}
