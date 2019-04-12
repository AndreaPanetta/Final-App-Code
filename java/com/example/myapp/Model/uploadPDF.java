/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       uploadPDF
 *
 * Description:     Model to help get data from the database
 *******************************************************************************************/

package com.example.myapp.Model;

public class uploadPDF {

    public String pdfname;
    public String url;

    public uploadPDF() {

    }

    public uploadPDF(String name, String url) {
        this.pdfname = name;
        this.url = url;
    }

    public String getName() {
        return pdfname;
    }

    public String getUrl() {
        return url;
    }

}
