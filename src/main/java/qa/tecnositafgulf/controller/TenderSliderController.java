package qa.tecnositafgulf.controller;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import qa.tecnositafgulf.model.tenders.Tender;
import qa.tecnositafgulf.service.TenderService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi on 1/6/18.
 */
public class TenderSliderController extends GenericForwardComposer {
    Label tenderTitle;
    Label tenderNumber;
    Label tenderCategory;
    Label tenderIssuingDate;
    Label tenderClosingDate;
    List<String> titleList=null, numberList=null, categoryList=null, issuingDateList=null, closingDateList=null;
    TenderService tenderService;
    private List<Tender> tenders;

    public void doAfterCompose (Component comp) throws Exception {
        super.doAfterCompose(comp);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        tenderService = context.getBean(TenderService.class);
        tenders = tenderService.listTenders();
        if(tenders.size()==0){
            tenderNumber.setValue("NO TENDER SO FAR");
        }else {
            tenderNumber.setValue("Tender " + tenders.get(0).getNumber());
            tenderTitle.setValue(tenders.get(0).getTitle());
            tenderCategory.setValue("Category: " + tenders.get(0).getType());
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy");
            tenderIssuingDate.setValue("Issuing Date : " + sdf.format(tenders.get(0).getIssuingDate()));
            tenderClosingDate.setValue("Closing Date : " + sdf.format(tenders.get(0).getClosingDate()));
            Clients.evalJavaScript("setTenderNumber('" + getTenderNumberString() + "')");
            Clients.evalJavaScript("setTenderTitle('" + getTenderTitleString() + "')");
            Clients.evalJavaScript("setTenderCategory('" + getTenderCategoryString() + "')");
            Clients.evalJavaScript("setTenderIssuingDate('" + getTenderIssuingDateString() + "')");
            Clients.evalJavaScript("setTenderClosingDate('" + getTenderClosingDateString() + "')");

            int delay = 5;
            if (tenders.size() > 1) {
                String command = "startTenderSlideShow(" + delay * 1000 + ")";
                System.out.println(command);
                Clients.evalJavaScript(command);
            }
        }
    }


    private String getTenderNumberString() {
        StringBuilder sb = new StringBuilder();
        List l = getNumberList();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0)
                sb.append("$$$")
                        ;
            sb.append(l.get(i));
        }
        return sb.toString();
    }

    private String getTenderTitleString() {
        StringBuilder sb = new StringBuilder();
        List l = getTitleList();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0)
                sb.append("$$$")
                        ;
            sb.append(l.get(i));
        }
        return sb.toString();
    }

    private String getTenderCategoryString() {
        StringBuilder sb = new StringBuilder();
        List l = getCategoryList();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0)
                sb.append("$$$")
                        ;
            sb.append(l.get(i));
        }
        return sb.toString();
    }

    private String getTenderIssuingDateString() {
        StringBuilder sb = new StringBuilder();
        List l = getIssuingDateList();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0)
                sb.append("$$$")
                        ;
            sb.append(l.get(i));
        }
        return sb.toString();
    }

    private String getTenderClosingDateString() {
        StringBuilder sb = new StringBuilder();
        List l = getClosingDateList();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0)
                sb.append("$$$")
                        ;
            sb.append(l.get(i));
        }
        return sb.toString();
    }

    private List<String> getNumberList () {
        if (numberList == null) {
            numberList = new ArrayList<String>();
            for (int i = 0; i < tenders.size(); i++) {
                numberList.add("Tender "+tenders.get(i).getNumber());
            }
        }
        return numberList;
    }

    private List<String> getTitleList () {
        if (titleList == null) {
            titleList = new ArrayList<String>();
            for (int i = 0; i < tenders.size(); i++) {
                titleList.add(tenders.get(i).getTitle());
            }
        }
        return titleList;
    }

    private List<String> getCategoryList () {
        if (categoryList == null) {
            categoryList = new ArrayList<String>();
            for (int i = 0; i < tenders.size(); i++) {
                categoryList.add("Category : "+tenders.get(i).getType());
            }
        }
        return categoryList;
    }

    private List<String> getIssuingDateList () {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy");
        if (issuingDateList == null) {
            issuingDateList = new ArrayList<String>();
            for (int i = 0; i < tenders.size(); i++) {
                issuingDateList.add("Issuing Date : "+sdf.format(tenders.get(i).getIssuingDate()));
            }
        }
        return issuingDateList;
    }

    private List<String> getClosingDateList () {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy");
        if (closingDateList == null) {
            closingDateList = new ArrayList<String>();
            for (int i = 0; i < tenders.size(); i++) {
                closingDateList.add("Closing Date : "+sdf.format(tenders.get(i).getClosingDate()));
            }
        }
        return closingDateList;
    }

    @Listen("onClick = #tender-descriptionDiv; onClick = #tender-container")
    public void onClick(){
        Executions.sendRedirect("/pages/tender/view-tenders.zul");
    }
}
