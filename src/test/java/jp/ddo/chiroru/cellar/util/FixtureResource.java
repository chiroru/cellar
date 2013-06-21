package jp.ddo.chiroru.cellar.util;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class FixtureResource
        extends ExternalResource {

    private Description desc;

    public Statement apply(Statement base, Description description) {
        this.desc = description;
        return super.apply(base, description);
    }

    @Override
    protected void before() throws Throwable {
        System.out.println(desc.getClassName());
        System.out.println(desc.getMethodName());
        Fixture a = desc.getAnnotation(Fixture.class);
        if (a == null) System.out.println("annotation is null");
/*        Method m = desc.getTestClass().getMethod(desc.getMethodName(), null);
        System.out.println(m.getName());
        Fixture b = m.getAnnotation(Fixture.class);
        if (b == null) System.out.println("annotation is null");*/
        QueryResourceLoader loader = new QueryResourceLoader(a.value());
        loader.load();
    }

    @Override
    protected void after() {
        super.after();
    }

}
