from locust import HttpLocust, TaskSet, task, between


def _get_image_part(file_path, file_content_type='application/vnd.openxmlformats-officedocument.wordprocessingml.document'):
    import os
    file_name = os.path.basename(file_path)
    file_content = open(file_path, 'rb')
    return file_name, file_content, file_content_type


class UserBehavior(TaskSet):
    def on_start(self):
        """ on_start is called when a Locust start before any task is scheduled """
        """ self.login() """
        print("Start")

    def on_stop(self):
        """ on_stop is called when the TaskSet is stopping """
        """ self.logout() """
        print("Finish")

    """ def login(self):
        self.client.post("/login", {"username":"ellen_key", "password":"education"})

    def logout(self):
        self.client.post("/logout", {"username":"ellen_key", "password":"education"}) """

    """ @task(2)
    def index(self):
        self.client.get("/") """

    """ @task(1)
    def profile(self):
        self.client.get("/books") """

    @task(1)
    def convert_docx(self):
        payload = {
            "type": "pdf"
        }
        files = {
            "file": _get_image_part("Certificado de Asistencia.docx")
        }        
        
        self.client.post(
            "/convertDocxPdf",
            data = payload,
            files = files,
        )


class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    wait_time = between(5, 9)
