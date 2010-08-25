package at.redcross.tacos.web.beans;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.reflections.Reflections;
import org.springframework.security.access.annotation.Secured;

@ApplicationScoped
@ManagedBean(name = "actionRegistryBean")
public class SecuredActionRegistry {

	/** the local list of available actions */
	private Set<String> actions;

	@PostConstruct
	protected void initRegistry() {
		actions = new HashSet<String>();
		Reflections reflections = new Reflections("at.redcross.tacos.web.beans");
		for (Method method : reflections.getMethodsAnnotatedWith(Secured.class)) {
			actions.add(getExpressionName(method));
		}
	}

	public Set<String> getActions() {
		return actions;
	}

	/** Builds the expression based on the given method */
	private String getExpressionName(Method method) {
		Class<?> clazz = method.getDeclaringClass();
		ManagedBean annotation = clazz.getAnnotation(ManagedBean.class);
		return "#{" + annotation.name() + "." + method.getName() + "}";
	}
}
