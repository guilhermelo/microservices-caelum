package br.com.caelum.eats.pagamento;

import lombok.Getter;
import org.springframework.hateoas.Link;

@Getter
public class LinkWithMethod extends Link {

    private final String method;

    public LinkWithMethod(Link link, String method) {
        super(link.getHref(), link.getRel());
        this.method = method;
    }
}
