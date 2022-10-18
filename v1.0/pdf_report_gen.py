# pip install fpdf
# pip install Image

import fpdf, os
import datetime
import PIL

class Writter:
    def __init__(self, path, pdf, cover_img):
        self.__path = path
        self.__doc = fpdf.FPDF()
        self.__pdf = pdf
        self.__cover_img = cover_img
        self.__epw = 0
        self.__map = {}
        self.__init()
        self.__name = ""
        self.__gender = ""
        self.__age = ""
        self.__date = ""
        self.__food = ""
        self.__diary = ""
        self.__state = ""
        self.__symp = ""
        self.__time = ""
        self.__type = ""
        self.__title = ""
        self.__desp = ""
        self.__image = ""
        self.__sysm = ""
        self.__addition = ""
        self.__info_table = ["name", "gender", "age"]
        self.__elimination_table = ["date", "food", "state"]
        self.__meal_diary_table = ["time", "type", "title", "desp", "image", "sysm", "addition"]
        self.__pre = self.__meal_diary_table.index("time")

    @property
    def n(self):
        return self.__n

    @n.setter
    def n(self, v):
        self.__n = v

    @property
    def time(self):
        return self.__time

    @time.setter
    def time(self, v):
        self.__time = v

    @property
    def type(self):
        return self.__type

    @type.setter
    def type(self, v):
        self.__type = v

    @property
    def title(self):
        return self.__title

    @title.setter
    def title(self, v):
        self.__title = v

    @property
    def desp(self):
        return self.__desp

    @desp.setter
    def desp(self, v):
        self.__desp = v

    @property
    def image(self):
        return self.__image

    @image.setter
    def image(self, v):
        self.__image = v

    @property
    def sysm(self):
        return self.__sysm

    @sysm.setter
    def sysm(self, v):
        self.__sysm = v

    @property
    def addition(self):
        return self.__addition

    @addition.setter
    def addition(self, v):
        self.__addition = v

    @property
    def symp(self):
        return self.__symp

    @symp.setter
    def symp(self, v):
        self.__symp = v

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, v):
        self.__name = v

    @property
    def gender(self):
        return self.__gender

    @gender.setter
    def gender(self, v):
        self.__gender = v

    @property
    def age(self):
        return self.__age

    @age.setter
    def age(self, v):
        self.__age = v

    @property
    def date(self):
        return self.__date

    @date.setter
    def date(self, v):
        self.__date = v

    @property
    def food(self):
        return self.__food

    @food.setter
    def food(self, v):
        self.__food = v

    @property
    def food_diary(self):
        return self.__diary

    @food.setter
    def food_diary(self, v):
        self.__diary = v

    @property
    def state(self):
        return self.__state

    @state.setter
    def state(self, v):
        self.__state = v

    def __create_map(self):
        self.__map["name"] = "Name"
        self.__map["gender"] = "Gender"
        self.__map["age"] = "Age"
        self.__map["date"] = "Date"
        self.__map["food"] = "Food ingredients Elimination"
        self.__map["diary"] = "Food Diary"
        self.__map["state"] = "State of elimination"
        self.__map["symp"] = "Symptoms Name and Time"
        self.__map["time"] = "Time"
        self.__map["type"] = "Type of Meal"
        self.__map["title"] = "Meal Title"
        self.__map["desp"] = "Description"
        self.__map["image"] = "Meal Image"
        self.__map["sysm"] = "Symptoms"
        self.__map["addition"] = "Additional Notes"

    def __init(self):
        os.makedirs(self.__path, exist_ok=True)
        self.__doc.add_page()
        self.__doc.set_font('Arial', 'B', 40)
        self.__doc.set_y(50)
        self.__doc.cell(0, 10, "Food Intolerance Report", align='C')
        w = self.__doc.w - 2 * self.__doc.l_margin
        self.__epw = w / 2
        t = 10
        w = self.__doc.w - 2 * self.__doc.l_margin - 2 * t
        img = PIL.Image.open(self.__cover_img)
        w_, h_ = img.size
        self.__doc.image(self.__cover_img, self.__doc.l_margin + t, y=100, w=w, h=w*h_/w_)
        self.__doc.set_font('Arial', '', 10)
        self.__doc.set_y(250)
        self.__doc.multi_cell(0, 10, "Powered by FoodInTol", align='C')
        self.__doc.multi_cell(0, 10, "{}".format(datetime.date.today()), align='C')
        self.__doc.add_page()
        self.__create_map()

    def write_info(self, v="info"):
        if v == "info":
            data, others = self.__info_table, None
            s = "Child information"
        if v == "elimination":
            data, others = self.__elimination_table[:self.__pre], self.__elimination_table[self.__pre:]
            s = "Elimination condition"
        if v == "meal_diary":
            data, others = self.__meal_diary_table[:self.__pre], self.__meal_diary_table[self.__pre:]
            s = ''
        # self.__doc.set_y(20)
        self.__doc.set_font('Arial', 'B', 20)
        self.__doc.cell(0, 10, s)
        self.__doc.ln(15)
        self.__doc.set_font('Arial', '', 10)
        h = 10
        for idx, i in enumerate(data):
            self.__doc.cell(self.__epw, h, self.__map[i], border=1)
            self.__doc.cell(self.__epw, h, self.__getattribute__(i), border=1)
            self.__doc.ln(h)
        self.__doc.ln(h)
        if others is not None:
            for idx, i in enumerate(others):
                h = 10
                if self.__getattribute__(i).endswith("jpg") or self.__getattribute__(i).endswith("png"):
                    h = 20
                self.__doc.cell(self.__epw, h, self.__map[i], border=1)
                if self.__getattribute__(i).endswith("jpg") or self.__getattribute__(i).endswith("png"):
                    self.__doc.image(self.__getattribute__(i), x=self.__doc.get_x() + 2, y=self.__doc.get_y() + 2, w=self.__epw / 5, h=h - 2)
                    self.__doc.cell(self.__epw, h, " ", border=1)
                else:
                    self.__doc.cell(self.__epw, h, self.__getattribute__(i), border=1)
                self.__doc.ln(h)
            self.__doc.ln(20)

    def save_pdf(self):
        self.__doc.output(self.__path + self.__pdf)