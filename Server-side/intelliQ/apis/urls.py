from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^generate_qa$', views.generate_qa, name='generate_qa'),
]
