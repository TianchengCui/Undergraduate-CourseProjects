class Flower(object):
    _FIELD_RULES = {
        "name": (str, "The input of the name is incorrect. A string is required"),
        "petals": (int, "The input of the number of petals is incorrect. An integer is required"),
        "price": (float, "The input of the price is incorrect. A float is required"),
    }

    def __init__(self, name, petals, price):
        self.name = ""
        self.petals = 0
        self.price = 0.0
        if (self._valid_type("name", name)
                and self._valid_type("petals", petals)
                and self._valid_type("price", price)):
            self.name, self.petals, self.price = name, petals, price

    def _valid_type(self, field, value):
        expected_type, message = self._FIELD_RULES[field]
        if type(value) is expected_type:
            return True
        print(message)
        return False

    def setName(self, name):
        if self._valid_type("name", name):
            self.name = name

    def setNumPetals(self, petals):
        if self._valid_type("petals", petals):
            self.petals = petals

    def setPrice(self, price):
        if self._valid_type("price", price):
            self.price = price

    def getName(self):
        return self.name

    def getNumPetals(self):
        return self.petals

    def getPrice(self):
        return self.price

    def getValue(self):
        return {"name": self.name, "numPetal": self.petals, "price": self.price}
