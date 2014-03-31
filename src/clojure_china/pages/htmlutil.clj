(ns clojure-china.pages.htmlutil
  (:require [noir.session :as session]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [ring.util.response :as response]))
;;控制页面flash的显示
(defn err-box [msg & code]
  [:div.alert.alert-error
   [:button {:type "button", :class "close", :data-dismiss "alert"} "&times;"] msg
   code])
(defn suc-box [msg & code]
  [:div.alert.alert-success
   [:button {:type "button", :class "close", :data-dismiss "alert"} "&times;"] msg
   code])
(defn msg-box [msg & code]
  [:div.alert.alert-info
   [:button {:type "button", :class "close", :data-dismiss "alert"} "&times;"] msg
   code])

(defn flash-err [url msg & code]
  (do
    (session/flash-put! :error (err-box msg code))
    (response/redirect url)))
(defn flash-suc [url msg & code]
  (do
    (session/flash-put! :success (suc-box msg code))
    (response/redirect url)))
(defn flash-msg [url msg & code]
  (do
    (session/flash-put! :success (msg-box msg code))
    (response/redirect url)))

;;页面模板
(defn head-template [code]
  (let [user (session/get :user)]
    (html5
      [:head
       [:title (:title code)]
       (include-css "http://clojure-china.qiniudn.com/bootstrap-theme.min.css")
       (include-css "http://clojure-china.qiniudn.com/bootstrap.min.css")
       #_(include-css "")
       [:body
        [:nav.navbar.navbar-inverse.navbar-static-top {:role "navigation"}
         [:div.navbar-header
          [:button.navbar-toggle 
           {:type "button" :data-toggle "collapse" :data-target "#bs-example-navbar-collapse-8"}
           [:span.sr-only ""]
           (for [x (range 3)]
             [:span.icon-bar])]]
         [:a.navbar-brand {:href "/"} "Clojure-China"]
         [:div.collapse.navbar-collapse#bs-example-navbar-collapse-8
          [:ul.nav.navbar-nav
           [:ul.nav.navbar-nav
            (for [[content url classes] '(["首页" "#" ""]
                                          ["Wiki" "#" ""]
                                          ["会员" "#" ""])]
              [:li [:a {:class classes :href url} content]])]]]]
        [:div.container code]
        (include-js "http://clojure-china.qiniudn.com/jquery.min.js")
        (include-js "http://clojure-china.qiniudn.com/bootstrap.min.js")
        ]])))

(defmacro def-page [page-name [& args] & code]
  `(defn ~page-name [~@args]
     (head-template (do ~@code))))
