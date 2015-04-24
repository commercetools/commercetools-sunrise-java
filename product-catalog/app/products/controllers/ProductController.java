package products.controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller {

    public static Result index() {
        return ok("Good!");
    }
}
