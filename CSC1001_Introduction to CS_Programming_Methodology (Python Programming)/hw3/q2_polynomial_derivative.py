import re


class Polynomial(object):
    def __init__(self, polynominal):
        self.polynominal = polynominal

    def derivative(self):
        polynomial = "".join(self.polynominal.split())
        terms = re.findall(r"[+-]?[^+-]+", polynomial)
        derivative_terms = []

        for term in terms:
            match = re.fullmatch(r"([+-]?)(?:(\d+)\*)?([A-Za-z])(?:\^(\d+))?", term)
            if match is None:
                continue  # Constant term.

            sign, coefficient, variable, exponent = match.groups()
            degree = int(exponent) if exponent is not None else 1
            magnitude = int(coefficient or "1") * degree
            if magnitude == 0:
                continue

            result = str(magnitude)
            if degree > 1:
                result += "*" + variable
            if degree > 2:
                result += "^" + str(degree - 1)

            if sign == "-":
                result = "-" + result
            elif derivative_terms:
                result = "+" + result
            derivative_terms.append(result)

        return "".join(derivative_terms) or "0"
