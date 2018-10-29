package com.ys.datatool.service.web;

import com.ys.datatool.domain.ExcelDatas;
import com.ys.datatool.domain.Product;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.ExportUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mo on @date  2018/7/10.
 * 车魔师系统
 */
@Service
public class CheMoShiService {

    private String SERVICEDETAIL_URL = "http://119.29.52.108/chemos/ct/viewCvtlist.nx?&cvtid=10226&pgsize=25&showStyle=GRID&selectedMode=MULTI&canSelect=true&canClick=false&openWindow=parent&addparam=PAR_A:&addparam=PAR_C:&pd=2&pm=2&viewName=/jsp/pages/cvtPages/cvt_0&param_tm=1531215266508&addparam=PAR_B:";

    private String SERVICE_URL = "http://119.29.52.108/chemos/ct/viewCvtlist.nx?&cvtid=10226&showStyle=GRID&selectedMode=MULTI&canSelect=true&canClick=false&openWindow=parent&pd=2&pm=2&viewName=/jsp/pages/cvtPages/cvt_0&CvtListCacheKey=CUSGRID&param_tm=1531213663209&id=10226:10206&pid=0&rid=0&isRefresh=0&isSort=0&page={no}";

    private String trName = "tr";

    private Charset charset = Charset.forName("gbk");

    private String trRegEx = "#list-con > div.main_con > table > tbody:nth-child(2) > tr";

    private String companyName = "车魔师";

    //获取数据的令牌
    private String COOKIE = "CUSGRID_CvtList_SortCol=; CUSGRID_CvtList_SortColName=; CUSGRID_CvtList_SortWay=; CUSGRID_CvtList_PageIndex=1; JSESSIONID=abcikm3byYXha7q7HAdsw; ROUTEID=.traf94; username=13531100432; ET=WEB";


    /**
     * 服务项目
     * @throws IOException
     */
    @Test
    public void fetchServiceData() throws IOException {
        List<Product> products = new ArrayList<>();
        Set<String> secondCategorySet = new HashSet<>();

        for (int i = 1; i <= 2; i++) {
            Response res = ConnectionUtil.doGetWithLeastParams(StringUtils.replace(SERVICE_URL, "{no}", String.valueOf(i)), COOKIE);
            String content = res.returnContent().asString(charset);
            Document doc = Jsoup.parseBodyFragment(content);

            int trSize = WebClientUtil.getTagSize(doc, trRegEx, trName);
            if (trSize > 0) {
                for (int j = 1; j <= trSize; j++) {

                    String secondCategoryNameRegEx = "#list-con > div.main_con > table > tbody:nth-child(2) > tr:nth-child({no}) > td:nth-child(5)";
                    String secondCategoryName = doc.select(StringUtils.replace(secondCategoryNameRegEx, "{no}", j + "")).text();

                    if ("类别".equals(secondCategoryName))
                        continue;

                    secondCategorySet.add(secondCategoryName);
                }
            }
        }

        if (secondCategorySet.size() > 0) {
            for (String secondCategory : secondCategorySet) {

                Response res = ConnectionUtil.doGetWithLeastParams(SERVICEDETAIL_URL + secondCategory, COOKIE);
                String content = res.returnContent().asString(charset);
                Document doc = Jsoup.parseBodyFragment(content);

                int trSize = WebClientUtil.getTagSize(doc, trRegEx, trName);
                if (trSize > 0) {
                    for (int i = 1; i <= trSize; i++) {


                        String productNameRegEx = "#list-con > div.main_con > table > tbody:nth-child(2) > tr:nth-child({no}) > td:nth-child(2)";
                        String firstCategoryNameRegEx = "#list-con > div.main_con > table > tbody:nth-child(2) > tr:nth-child({no}) > td:nth-child(3)";
                        String secondCategoryNameRegEx = "#list-con > div.main_con > table > tbody:nth-child(2) > tr:nth-child({no}) > td:nth-child(5)";

                        String productName = doc.select(StringUtils.replace(productNameRegEx, "{no}", String.valueOf(i))).text();
                        ;
                        String firstCategoryName = doc.select(StringUtils.replace(firstCategoryNameRegEx, "{no}", String.valueOf(i))).text();
                        String secondCategoryName = doc.select(StringUtils.replace(secondCategoryNameRegEx, "{no}", String.valueOf(i))).text();

                        Product product = new Product();
                        product.setProductName(productName);
                        product.setFirstCategoryName(firstCategoryName);
                        product.setSecondCategoryName(secondCategoryName);
                        product.setItemType("服务项");
                        products.add(product);
                    }
                }
            }
        }

        String pathname = "C:\\exportExcel\\车魔师服务导出.xlsx";
        ExportUtil.exportProductDataInLocal(products, ExcelDatas.workbook, pathname);

    }


}
