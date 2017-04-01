package uk.co.nyakeh.stacks.objects;

import java.util.Date;
import java.util.UUID;

public class Dividend {
    public UUID Id;
    public Date Date;
    public double Amount;

    public Dividend(UUID id, Date date, double amount) {
        Id = id;
        Date = date;
        Amount = amount;
    }
}