(ns example.core
  (:require [io.factorhouse.storybook.core :as storybook]))

(defn button [{:keys [variant size disabled on-click class]
               :or {variant :primary size :md}}
              & children]
  (let [base-classes "inline-flex items-center justify-center font-medium rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed"
        variant-classes (case variant
                          :primary "bg-blue-600 hover:bg-blue-700 text-white focus:ring-blue-500"
                          :secondary "bg-gray-200 hover:bg-gray-300 text-gray-900 focus:ring-gray-500"
                          :danger "bg-red-600 hover:bg-red-700 text-white focus:ring-red-500"
                          :success "bg-green-600 hover:bg-green-700 text-white focus:ring-green-500"
                          :outline "border-2 border-gray-300 hover:border-gray-400 bg-transparent text-gray-700 focus:ring-gray-500"
                          :ghost "hover:bg-gray-100 text-gray-700 focus:ring-gray-500")
        size-classes (case size
                       :xs "px-2 py-1 text-xs"
                       :sm "px-3 py-1.5 text-sm"
                       :md "px-4 py-2 text-sm"
                       :lg "px-5 py-2.5 text-base"
                       :xl "px-6 py-3 text-lg")]
    [:button {:class (str base-classes " " variant-classes " " size-classes " " class)
              :disabled disabled
              :on-click on-click}
     children]))

(defmethod storybook/story "Component/Buttons/Primary" [_]
  {:component button
   :stories {:Default {:args {:props {} :children ["Primary Button"]}}
             :Large {:args {:props {:size :lg} :children ["Large Primary"]}}
             :Small {:args {:props {:size :sm} :children ["Small Primary"]}}
             :Disabled {:args {:props {:disabled true} :children ["Disabled"]}}
             :WithClick {:args {:props {:on-click #(js/alert "Primary clicked!")} :children ["Click Me"]}}}})

(defmethod storybook/story "Component/Buttons/Secondary" [_]
  {:component button
   :stories {:Default {:args {:props {:variant :secondary} :children ["Secondary Button"]}}
             :Large {:args {:props {:variant :secondary :size :lg} :children ["Large Secondary"]}}
             :WithIcon {:args {:props {:variant :secondary} :children ["Secondary"]}}}})

(defmethod storybook/story "Component/Buttons/Danger" [_]
  {:component button
   :stories {:Default {:args {:props {:variant :danger} :children ["Delete"]}}
             :Confirm {:args {:props {:variant :danger :on-click #(js/confirm "Are you sure?")} :children ["Confirm Delete"]}}
             :Small {:args {:props {:variant :danger :size :sm} :children ["Remove"]}}}})

(defmethod storybook/story "Component/Buttons/Variants" [_]
  {:component button
   :stories {:Primary {:args {:props {:variant :primary} :children ["Primary"]}}
             :Secondary {:args {:props {:variant :secondary} :children ["Secondary"]}}
             :Danger {:args {:props {:variant :danger} :children ["Danger"]}}
             :Success {:args {:props {:variant :success} :children ["Success"]}}
             :Outline {:args {:props {:variant :outline} :children ["Outline"]}}
             :Ghost {:args {:props {:variant :ghost} :children ["Ghost"]}}}})

;; =============================================================================
;; INPUTS
;; =============================================================================

(defn input [{:keys [type placeholder value disabled error label class on-change]
              :or {type "text"}}]
  [:div {:class "space-y-1"}
   (when label
     [:label {:class "block text-sm font-medium text-gray-700"} label])
   [:input {:type type
            :placeholder placeholder
            :value value
            :disabled disabled
            :on-change on-change
            :class (str "block w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-offset-0 transition-colors "
                        (if error
                          "border-red-300 focus:border-red-500 focus:ring-red-500"
                          "border-gray-300 focus:border-blue-500 focus:ring-blue-500")
                        " " class)}]
   (when error
     [:p {:class "text-sm text-red-600"} error])])

(defmethod storybook/story "Component/Forms/Input/Text" [_]
  {:component input
   :stories {:Default {:args {:props {:placeholder "Enter text..."} :children []}}
             :WithLabel {:args {:props {:label "Username" :placeholder "Enter username"} :children []}}
             :WithValue {:args {:props {:label "Email" :value "user@example.com"} :children []}}
             :WithError {:args {:props {:label "Password" :type "password" :error "Password is required"} :children []}}
             :Disabled {:args {:props {:label "Disabled" :placeholder "Cannot edit" :disabled true} :children []}}}})

(defmethod storybook/story "Component/Forms/Input/Email" [_]
  {:component input
   :stories {:Default {:props {:type "email" :placeholder "user@example.com" :label "Email Address"} :children []}
             :WithValidation {:props {:type "email" :label "Email" :value "invalid-email" :error "Please enter a valid email"} :children []}}})

;; =============================================================================
;; CARDS
;; =============================================================================

(defn card [{:keys [title subtitle children class actions]}]
  [:div {:class (str "bg-white rounded-lg border border-gray-200 shadow-sm " class)}
   (when (or title subtitle)
     [:div {:class "px-6 py-4 border-b border-gray-200"}
      (when title
        [:h3 {:class "text-lg font-medium text-gray-900"} title])
      (when subtitle
        [:p {:class "mt-1 text-sm text-gray-500"} subtitle])])
   (when children
     [:div {:class "px-6 py-4"} children])
   (when actions
     [:div {:class "px-6 py-4 bg-gray-50 border-t border-gray-200 rounded-b-lg"}
      actions])])

(defmethod storybook/story "Component/Layout/Card/Basic" [_]
  {:component card
   :stories {:Default {:args {:title "Card Title"
                              :children "This is the card content area where you can put any content."}}
             :WithSubtitle {:args {:title "User Profile"
                                   :subtitle "Manage your account settings"
                                   :children "Profile information and settings go here."}}
             :WithActions {:args {:title "Confirmation"
                                  :children "Are you sure you want to delete this item?"
                                  :actions [:div {:class "flex space-x-2"}
                                            [button {:variant :outline :size :sm} "Cancel"]
                                            [button {:variant :danger :size :sm} "Delete"]]}}}})

;; =============================================================================
;; BADGES
;; =============================================================================

(defn badge [{:keys [variant size class]
              :or {variant :default size :md}} & children]
  (let [base-classes "inline-flex items-center font-medium rounded-full"
        variant-classes (case variant
                          :default "bg-gray-100 text-gray-800"
                          :primary "bg-blue-100 text-blue-800"
                          :success "bg-green-100 text-green-800"
                          :warning "bg-yellow-100 text-yellow-800"
                          :danger "bg-red-100 text-red-800"
                          :info "bg-indigo-100 text-indigo-800")
        size-classes (case size
                       :sm "px-2 py-0.5 text-xs"
                       :md "px-2.5 py-1 text-sm"
                       :lg "px-3 py-1.5 text-base")]
    [:span {:class (str base-classes " " variant-classes " " size-classes " " class)}
     children]))

(defmethod storybook/story "Component/Display/Badge/Status" [_]
  {:component badge
   :stories {:Active {:args {:props {:variant :success} :children ["Active"]}}
             :Pending {:args {:props {:variant :warning} :children ["Pending"]}}
             :Inactive {:args {:props {:variant :danger} :children ["Inactive"]}}
             :Draft {:args {:props {:variant :default} :children ["Draft"]}}}})

(defmethod storybook/story "Component/Display/Badge/Sizes" [_]
  {:component badge
   :stories {:Small {:args {:props {:variant :primary :size :sm} :children ["Small"]}}
             :Medium {:args {:props {:variant :primary :size :md} :children ["Medium"]}}
             :Large {:args {:props {:variant :primary :size :lg} :children ["Large"]}}}})

;; =============================================================================
;; ALERTS
;; =============================================================================

(defn alert [{:keys [variant title dismissible on-dismiss class]
              :or {variant :info}} & children]
  (let [base-classes "rounded-md p-4"
        variant-classes (case variant
                          :info "bg-blue-50 border border-blue-200"
                          :success "bg-green-50 border border-green-200"
                          :warning "bg-yellow-50 border border-yellow-200"
                          :danger "bg-red-50 border border-red-200")
        text-classes (case variant
                       :info "text-blue-800"
                       :success "text-green-800"
                       :warning "text-yellow-800"
                       :danger "text-red-800")]
    [:div {:class (str base-classes " " variant-classes " " class)}
     [:div {:class "flex"}
      [:div {:class "flex-1"}
       (when title
         [:h3 {:class (str "text-sm font-medium " text-classes)} title])
       [:div {:class (str "text-sm " (if title "mt-2" "") " " text-classes)}
        children]]
      (when dismissible
        [:div {:class "ml-auto pl-3"}
         [:button {:class (str "inline-flex rounded-md p-1.5 hover:bg-opacity-20 " text-classes)
                   :on-click on-dismiss}
          "×"]])]]))

(defmethod storybook/story "Component/Feedback/Alert/Types" [_]
  {:component alert
   :stories {:Info {:args {:props {:variant :info :title "Information"} :children ["This is an informational message."]}}
             :Success {:args {:props {:variant :success :title "Success!"} :children ["Your changes have been saved."]}}
             :Warning {:args {:props {:variant :warning :title "Warning"} :children ["Please review your input."]}}
             :Danger {:args {:props {:variant :danger :title "Error"} :children ["Something went wrong."]}}}})

(defmethod storybook/story "Component/Feedback/Alert/Dismissible" [_]
  {:component alert
   :stories {:Default {:args {:props {:variant :info
                                      :title "Dismissible Alert"
                                      :dismissible true
                                      :on-dismiss #(js/alert "Alert dismissed!")}
                              :children ["Click the × to dismiss this alert."]}}}})

;; =============================================================================
;; NAVIGATION
;; =============================================================================

(defn nav-item [{:keys [active href on-click class]} & children]
  [:a {:href href
       :on-click on-click
       :class (str "px-3 py-2 text-sm font-medium rounded-md transition-colors "
                   (if active
                     "bg-blue-100 text-blue-700"
                     "text-gray-600 hover:text-gray-900 hover:bg-gray-100")
                   " " class)}
   children])

(defn navigation [{:keys [items class]}]
  [:nav {:class (str "flex space-x-1 " class)}
   (for [item items]
     [nav-item (:props item) (:children item)])])

(defmethod storybook/story "Component/Navigation/NavItem" [_]
  {:component nav-item
   :stories {:Default {:args {:props {:href "#"} :children ["Dashboard"]}}
             :Active {:args {:props {:active true :href "#"} :children ["Settings"]}}
             :WithClick {:args {:props {:on-click #(js/alert "Logging out...")} :children ["Logout"]}}}})

(defmethod storybook/story "Component/Navigation/Horizontal" [_]
  {:component navigation
   :stories {:Default {:args {:props {:items [{:props {:href "#" :active true} :children ["Dashboard"]}
                                              {:props {:href "#"} :children ["Projects"]}
                                              {:props {:href "#"} :children ["Team"]}
                                              {:props {:href "#"} :children ["Settings"]}]}
                              :children []}}}})

;; =============================================================================
;; LOADING STATES
;; =============================================================================

(defn spinner [{:keys [size class]
                :or {size :md}}]
  (let [size-classes (case size
                       :sm "w-4 h-4"
                       :md "w-6 h-6"
                       :lg "w-8 h-8"
                       :xl "w-12 h-12")]
    [:div {:class (str "animate-spin rounded-full border-2 border-gray-300 border-t-blue-600 " size-classes " " class)}]))

(defn loading-button [{:keys [loading] :as props} & children]
  [button (assoc props
                 :disabled loading)
   (if loading
     [:div {:class "flex items-center space-x-2"}
      [spinner {:size :sm}]
      [:span "Loading..."]]
     children)])

(defmethod storybook/story "Component/Feedback/Spinner" [_]
  {:component spinner
   :stories {:Small {:args {:props {:size :sm} :children []}}
             :Medium {:args {:props {:size :md} :children []}}
             :Large {:args {:props {:size :lg} :children []}}
             :ExtraLarge {:args {:props {:size :xl} :children []}}}})

(defmethod storybook/story "Component/Buttons/Loading" [_]
  {:component loading-button
   :stories {:Default {:args {:props {} :children ["Submit"]}}
             :Loading {:args {:props {:loading true} :children ["Submit"]}}
             :LoadingPrimary {:args {:props {:loading true :variant :primary} :children ["Save Changes"]}}}})

;; =============================================================================
;; MODALS/OVERLAYS
;; =============================================================================

(defn modal [{:keys [open title children actions on-close class]}]
  (when open
    [:div {:class "fixed inset-0 z-50 flex items-center justify-center"}
     ;; Backdrop
     [:div {:class "fixed inset-0 bg-black bg-opacity-50"
            :on-click on-close}]
     ;; Modal
     [:div {:class (str "relative bg-white rounded-lg shadow-xl max-w-md w-full mx-4 " class)}
      [:div {:class "px-6 py-4 border-b border-gray-200"}
       [:div {:class "flex items-center justify-between"}
        [:h3 {:class "text-lg font-medium text-gray-900"} title]
        [:button {:class "text-gray-400 hover:text-gray-600"
                  :on-click on-close}
         "×"]]]
      [:div {:class "px-6 py-4"} children]
      (when actions
        [:div {:class "px-6 py-4 bg-gray-50 border-t border-gray-200 flex justify-end space-x-2"}
         actions])]]))

(defmethod storybook/story "Component/Overlay/Modal" [_]
  {:component modal
   :stories {:Default {:args {:open true
                              :title "Confirm Action"
                              :children "Are you sure you want to perform this action?"
                              :actions [[:div {:class "flex space-x-2"}
                                         [button {:variant :outline :size :sm} "Cancel"]
                                         [button {:variant :primary :size :sm} "Confirm"]]]}}
             :Warning {:args {:open true
                              :title "Delete Item"
                              :children "This action cannot be undone. Are you sure?"
                              :actions [[:div {:class "flex space-x-2"}
                                         [button {:variant :outline :size :sm} "Cancel"]
                                         [button {:variant :danger :size :sm} "Delete"]]]}}}})