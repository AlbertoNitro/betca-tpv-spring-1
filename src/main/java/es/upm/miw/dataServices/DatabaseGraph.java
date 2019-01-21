package es.upm.miw.dataServices;

import es.upm.miw.documents.*;

import java.util.ArrayList;
import java.util.List;

public class DatabaseGraph {

    private List<User> userList;

    private List<Article> articleList;

    private List<CashierClosure> cashierClosureList;

    private List<FamilyArticle> familyArticleList;

    private List<FamilyComposite> familyCompositeList;

    private List<Invoice> invoiceList;

    private List<Provider> providerList;

    private List<Ticket> ticketList;

    private List<Shopping> shoppingList;

    private List<Voucher> voucherList;

    public DatabaseGraph() {
        this.userList = new ArrayList<>();
        this.articleList = new ArrayList<>();
        this.cashierClosureList = new ArrayList<>();
        this.familyArticleList = new ArrayList<>();
        this.familyCompositeList = new ArrayList<>();
        this.invoiceList = new ArrayList<>();
        this.providerList = new ArrayList<>();
        this.shoppingList = new ArrayList<>();
        this.ticketList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.voucherList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public List<CashierClosure> getCashierClosureList() {
        return cashierClosureList;
    }

    public void setCashierClosureList(List<CashierClosure> cashierClosureList) {
        this.cashierClosureList = cashierClosureList;
    }

    public List<FamilyArticle> getFamilyArticleList() {
        return familyArticleList;
    }

    public void setFamilyArticleList(List<FamilyArticle> familyArticleList) {
        this.familyArticleList = familyArticleList;
    }

    public List<FamilyComposite> getFamilyCompositeList() {
        return familyCompositeList;
    }

    public void setFamilyCompositeList(List<FamilyComposite> familyCompositeList) {
        this.familyCompositeList = familyCompositeList;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public List<Provider> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<Provider> providerList) {
        this.providerList = providerList;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public List<Shopping> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Shopping> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<Voucher> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }
}
