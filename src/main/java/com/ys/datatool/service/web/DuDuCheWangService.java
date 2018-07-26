package com.ys.datatool.service.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.datatool.domain.CarInfo;
import com.ys.datatool.domain.Product;
import com.ys.datatool.util.ConnectionUtil;
import com.ys.datatool.util.WebClientUtil;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on @date  2018/6/30.
 * 嘟嘟车网
 */
@Service
public class DuDuCheWangService {

    private static final String GOOD_URL = "http://new.duduchewang.com/servlet/getProductAjax?&shopcode=0010035";

    private static final String CARINFO_URL = "http://new.duduchewang.com/dataManage/customer/custInfoAjax.jsp?action=queryCustList&state=1";

    private static final String ACCEPT = "application/json, text/javascript, */*; q=0.01";

    private static final String CONNECTION = "keep-alive";

    private static final String HOST = "new.duduchewang.com";

    private static final String ORIGIN = "http://new.duduchewang.com";

    private static final String REFERER = "http://new.duduchewang.com/dataManage/customer/CustomerList.jsp?moduleid=44";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

    private static final String X_REQUESTED_WITH = "XMLHttpRequest";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String fileName = "嘟嘟车网";

    private String pageName = "total";

    private static final String COOKIE = "JSESSIONID=233E43D672AC2AB27F884935AD5DE655";


    /**
     *商品
     * @throws IOException
     */
    public void fetchItemData() throws IOException {
        List<Product> products = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(GOOD_URL, getGoodParams(String.valueOf(1)), COOKIE);
        int total = WebClientUtil.getTotalPage(response,MAPPER,pageName);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                response = ConnectionUtil.doPostWithLeastParams(GOOD_URL, getGoodParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    Product product = new Product();
                    product.setProductName(element.get("ModuleName").asText());
                    product.setCode(element.get("spbm").asText());
                    product.setBarCode(element.get("tiaoxingma").asText());
                    product.setProductCode(element.get("spbm").asText());
                    product.setFirstCategoryName(element.get("dlname").asText());
                    product.setSecondCategoryName(element.get("xlname").asText());
                    product.setItemType("配件");
                    product.setUnit(element.get("jldwname").asText());
                    product.setBrandName(element.get("project_brand").asText());
                    product.setPrice(element.get("shoujia").asText());
                    product.setCarModel(element.get("chexing").asText());
                    products.add(product);
                }
            }
        }
    }

    /**
     * 车辆信息
     * @throws IOException
     */
    public void fetchCarInfoData() throws IOException {
        List<CarInfo> carInfos = new ArrayList<>();

        Response response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getCarInfoParams(String.valueOf(1)), COOKIE);
        int total = WebClientUtil.getTotalPage(response,MAPPER,pageName);

        if (total > 0) {
            for (int i = 1; i <= total; i++) {
                response = ConnectionUtil.doPostWithLeastParams(CARINFO_URL, getCarInfoParams(String.valueOf(i)), COOKIE);
                JsonNode result = MAPPER.readTree(response.returnContent().asString());

                Iterator<JsonNode> it = result.get("rows").iterator();
                while (it.hasNext()) {
                    JsonNode element = it.next();

                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(element.get("CarHaoPai").asText());
                    carInfo.setName(element.get("CustomerName").asText());
                    carInfo.setPhone(element.get("CustomerMobile").asText());
                    carInfo.setBrand(element.get("cpinpai").asText());
                    carInfo.setCarModel(element.get("cchexi").asText());
                    carInfo.setVINcode(element.get("CheJiaHao").asText());
                    carInfos.add(carInfo);
                }
            }
        }
    }

    private List<BasicNameValuePair> getGoodParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("_search", "false"));
        params.add(new BasicNameValuePair("nd", "1509616521070"));
        params.add(new BasicNameValuePair("rows", "20"));
        params.add(new BasicNameValuePair("sidx", "CreateTime"));
        params.add(new BasicNameValuePair("sord", "desc"));
        params.add(new BasicNameValuePair("page", pageNo));
        return params;
    }

    private List<BasicNameValuePair> getCarInfoParams(String pageNo) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("_search", "false"));
        params.add(new BasicNameValuePair("nd", "1509613080572"));
        params.add(new BasicNameValuePair("rows", "20"));
        params.add(new BasicNameValuePair("sidx", "shopcode,createdtime desc,CustomerName"));
        params.add(new BasicNameValuePair("sord", ""));
        params.add(new BasicNameValuePair("page", pageNo));
        return params;
    }
}
