package guru.springframework.spring7resttemplate.client;

import org.springframework.data.domain.Page;

import guru.springframework.spring7resttemplate.model.BeerDTO;

public class BeerClientImpl implements BeerClient {
    @Override
    public Page<BeerDTO> listBeers() {
        return null;
    }
}