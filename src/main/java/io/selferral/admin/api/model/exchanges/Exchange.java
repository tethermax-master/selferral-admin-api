package io.selferral.admin.api.model.exchanges;

import org.springframework.web.client.RestTemplate;

import io.selferral.admin.api.core.CD.ExchangeName;
import io.selferral.admin.api.model.dto.ApiKeyDto;

public class Exchange {
    private ExchangeConnect conn;

    public Exchange(ExchangeName name, ApiKeyDto key, RestTemplate rest) {
        if(ExchangeName.BINGX.equals(name)) {
        	this.conn = (ExchangeConnect)new BingX(key, rest);
        } else if(ExchangeName.BITGET.equals(name)) {
        	this.conn = (ExchangeConnect)new Bitget(key, rest);
        } else if(ExchangeName.BITMART.equals(name)) {
        	this.conn = (ExchangeConnect)new BitMart(key, rest);
        } else if(ExchangeName.BYBIT.equals(name)) {
        	this.conn = (ExchangeConnect)new Bybit(key, rest);
        } else if(ExchangeName.COINCATCH.equals(name)) {
        	this.conn = (ExchangeConnect)new CoinCatch(key, rest);
        } else if(ExchangeName.DEEPCOIN.equals(name)) {
        	this.conn = (ExchangeConnect)new DeepCoin(key, rest);
        } else if(ExchangeName.MEXC.equals(name)) {
        	this.conn = (ExchangeConnect)new MEXC(key, rest);
        } else if(ExchangeName.OKX.equals(name)) {
        	this.conn = (ExchangeConnect)new OKX(key, rest);
        } else {
        	this.conn = null;
        }
    }

    public String getApiUid(String uid) throws Exception {
        return conn.getCustomer(uid);
    }


}
