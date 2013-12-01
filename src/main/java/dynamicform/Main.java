package dynamicform;

import com.lowagie.text.DocumentException;

import java.io.IOException;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
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
public class Main {
    public static void main(String[] args) throws IOException, DocumentException {
        Form form = new Form("請求書");
        form.setNote("お支払い条件: 月末締め翌月末払い\n" +
                "お支払方法: 銀行振込\n" +
                "子供銀行 大人支店 支店コード: 765\n" +
                "普通口座 0123456 口座名義 オレダヨオレ\n" +
                "振込手数料はお客様のご負担とさせていただきます。");
        form.setMeta1("請求日");
        form.setMeta1Value(JapaneseDate.now().format(DateTimeFormatter.ofPattern("平成y年M月d日", Locale.JAPANESE)));
        form.setMeta2("請求書番号");
        form.setMeta2Value("01234");
        form.setIssuedBy("俺だよ、俺");
        form.setIssuedTo("母ちゃん");

        form.addItem(new Item("会社のお金つかいこんじゃってさ・・・", 1, 1000000));
        form.saveAs("out.pdf","form.pdf", new String[]{});
    }
}