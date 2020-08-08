package com.ergasia;

public class Exchange {

    private Double amount;
    private Currency from;
    private Currency to;
    private Double rate;
    private Double exchangeAmount;

    public static class Builder {

        private Double amount;
        private Currency from;
        private Currency to;
        private Double rate;
        private Double exchangeAmount;

        public Builder() {}

        public Builder withAmount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder withFromCurrency(Currency from) {
            this.from = from;
            return this;
        }

        public Builder withToCurrency(Currency to) {
            this.to = to;
            return this;
        }

        public Builder withRate(Double rate) {
            this.rate = rate;
            return this;
        }

        public Builder withExchangeAmount(Double exchangeAmount) {
            this.exchangeAmount = exchangeAmount;
            return this;
        }

        public Exchange build() {
            Exchange exchange = new Exchange();
            exchange.amount = this.amount;
            exchange.from = this.from;
            exchange.to = this.to;
            exchange.rate = this.rate;
            exchange.exchangeAmount = this.exchangeAmount;
            return exchange;
        }
    }

    private Exchange() {}

    public Double getAmount() {
        return amount;
    }

    public Currency getFrom() {
        return from;
    }

    public Currency getTo() {
        return to;
    }

    public Double getRate() {
        return rate;
    }

    public Double getExchangeAmount() {
        return exchangeAmount;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "amount=" + amount +
                ", from=" + from +
                ", to=" + to +
                ", rate=" + rate +
                ", exchangeAmount=" + exchangeAmount +
                '}';
    }
}
