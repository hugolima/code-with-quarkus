package com.mybank;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
            title="My Bank API",
            version = "1.0.1",
            contact = @Contact(
                name = "API Support",
                url = "http://mybank.com/contact",
                email = "techsupport@mybank.com"),
            license = @License(
                name = "Apache 2.0",
                url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
    )
public class ApiApplication extends Application {

}
