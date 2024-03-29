package fr.miage.conference.api.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class GenericRsqlSpecification<T> implements Specification<T> {

    private String property;
    private transient ComparisonOperator operator;
    private List<String> arguments;

    public GenericRsqlSpecification(String selector, ComparisonOperator operator, List<String> arguments) {
        super();
        this.property = selector;
        this.operator = operator;
        this.arguments = arguments;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<? extends Serializable> args = castArguments(root);
        Object argument = args.get(0);
        switch (RsqlSearchOperation.getSimpleOperator(operator)) {

            case EQUAL: {
                if (argument instanceof String) {
                    return builder.like(root.get(property), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNull(root.get(property));
                } else {
                    return builder.equal(root.get(property), argument);
                }
            }
            case NOT_EQUAL: {
                if (argument instanceof String) {
                    return builder.notLike(root.<String> get(property), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNotNull(root.get(property));
                } else {
                    return builder.notEqual(root.get(property), argument);
                }
            }
            case GREATER_THAN: {
                return builder.greaterThan(root.<String> get(property), argument.toString());
            }
            case GREATER_THAN_OR_EQUAL: {
                return builder.greaterThanOrEqualTo(root.<String> get(property), argument.toString());
            }
            case LESS_THAN: {
                return builder.lessThan(root.<String> get(property), argument.toString());
            }
            case LESS_THAN_OR_EQUAL: {
                return builder.lessThanOrEqualTo(root.<String> get(property), argument.toString());
            }
            case IN:
                return root.get(property).in(args);
            case NOT_IN:
                return builder.not(root.get(property).in(args));
        }

        return null;
    }

    private List<? extends Serializable> castArguments(final Root<T> root) {

        Class<? extends Object> type = root.get(property).getJavaType();

        return arguments.stream().map(arg -> {
            if (type.equals(Integer.class)) {
                return Integer.parseInt(arg);
            } else if (type.equals(Long.class)) {
                return Long.parseLong(arg);
            } else {
                return arg;
            }
        }).toList();
    }

    // standard constructor, getter, setter

    public enum RsqlSearchOperation {
        EQUAL(RSQLOperators.EQUAL),
        NOT_EQUAL(RSQLOperators.NOT_EQUAL),
        GREATER_THAN(RSQLOperators.GREATER_THAN),
        GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL),
        LESS_THAN(RSQLOperators.LESS_THAN),
        LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL),
        IN(RSQLOperators.IN),
        NOT_IN(RSQLOperators.NOT_IN);

        private ComparisonOperator operator;

        private RsqlSearchOperation(ComparisonOperator operator) {
            this.operator = operator;
        }

        public static RsqlSearchOperation getSimpleOperator(ComparisonOperator operator) {
            for (RsqlSearchOperation operation : values()) {
                if (operation.getOperator() == operator) {
                    return operation;
                }
            }
            return RsqlSearchOperation.EQUAL;
        }

        public ComparisonOperator getOperator() {
            return operator;
        }
    }
}