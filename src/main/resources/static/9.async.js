(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[9],{"5WY0":function(e,t,a){e.exports={main:"antd-pro\\pages\\-user\\-register-main",getCaptcha:"antd-pro\\pages\\-user\\-register-getCaptcha",submit:"antd-pro\\pages\\-user\\-register-submit",login:"antd-pro\\pages\\-user\\-register-login",error:"antd-pro\\pages\\-user\\-register-error",success:"antd-pro\\pages\\-user\\-register-success",warning:"antd-pro\\pages\\-user\\-register-warning","progress-pass":"antd-pro\\pages\\-user\\-register-progress-pass",progress:"antd-pro\\pages\\-user\\-register-progress"}},cq3J:function(e,t,a){"use strict";a.r(t),a.d(t,"default",function(){return F});a("14J3");var r,s,i,l=a("BMrR"),n=(a("+L6B"),a("2/Rp")),o=(a("jCWc"),a("kPKH")),c=(a("Q9mQ"),a("diRs")),p=(a("MXD1"),a("CFYs")),m=a("p0pE"),g=a.n(m),h=(a("5NDa"),a("5rEg")),u=(a("OaEy"),a("2fM7")),d=(a("y8nQ"),a("Vl3Y")),v=a("q1tI"),f=a.n(v),E=a("MuoO"),w=a("wY1l"),b=a.n(w),y=a("3a4m"),S=a.n(y),k=a("5WY0"),C=a.n(k),P=d["a"].Item,x=u["a"].Option,N=h["a"].Group,D={ok:f.a.createElement("div",{className:C.a.success},"\u5f3a\u5ea6\uff1a\u5f3a"),pass:f.a.createElement("div",{className:C.a.warning},"\u5f3a\u5ea6\uff1a\u4e2d"),poor:f.a.createElement("div",{className:C.a.error},"\u5f3a\u5ea6\uff1a\u592a\u77ed")},z={ok:"success",pass:"normal",poor:"exception"},F=(r=Object(E["connect"])(e=>{var t=e.register,a=e.loading;return{register:t,submitting:a.effects["register/submit"]}}),s=d["a"].create(),r(i=s(i=class extends v["Component"]{constructor(){super(...arguments),this.state={count:0,confirmDirty:!1,visible:!1,help:"",prefix:"86"},this.onGetCaptcha=(()=>{var e=59;this.setState({count:e}),this.interval=setInterval(()=>{e-=1,this.setState({count:e}),0===e&&clearInterval(this.interval)},1e3)}),this.getPasswordStatus=(()=>{var e=this.props.form,t=e.getFieldValue("password");return t&&t.length>9?"ok":t&&t.length>5?"pass":"poor"}),this.handleSubmit=(e=>{e.preventDefault();var t=this.props,a=t.form,r=t.dispatch;a.validateFields({force:!0},(e,t)=>{if(!e){var a=this.state.prefix;r({type:"register/submit",payload:g()({},t,{prefix:a})})}})}),this.handleConfirmBlur=(e=>{var t=e.target.value,a=this.state.confirmDirty;this.setState({confirmDirty:a||!!t})}),this.checkConfirm=((e,t,a)=>{var r=this.props.form;t&&t!==r.getFieldValue("password")?a("\u4e24\u6b21\u8f93\u5165\u7684\u5bc6\u7801\u4e0d\u5339\u914d!"):a()}),this.checkPassword=((e,t,a)=>{var r=this.state,s=r.visible,i=r.confirmDirty;if(t)if(this.setState({help:""}),s||this.setState({visible:!!t}),t.length<6)a("error");else{var l=this.props.form;t&&i&&l.validateFields(["confirm"],{force:!0}),a()}else this.setState({help:"\u8bf7\u8f93\u5165\u5bc6\u7801\uff01",visible:!!t}),a("error")}),this.changePrefix=(e=>{this.setState({prefix:e})}),this.renderPasswordProgress=(()=>{var e=this.props.form,t=e.getFieldValue("password"),a=this.getPasswordStatus();return t&&t.length?f.a.createElement("div",{className:C.a["progress-".concat(a)]},f.a.createElement(p["a"],{status:z[a],className:C.a.progress,strokeWidth:6,percent:10*t.length>100?100:10*t.length,showInfo:!1})):null})}componentDidUpdate(){var e=this.props,t=e.form,a=e.register,r=e.dispatch,s=t.getFieldValue("mail");"ok"===a.status&&r(S.a.push({pathname:"/user/register-result",state:{account:s}}))}componentWillUnmount(){clearInterval(this.interval)}render(){var e=this.props,t=e.form,a=e.submitting,r=t.getFieldDecorator,s=this.state,i=s.count,p=s.prefix,m=s.help,g=s.visible;return f.a.createElement("div",{className:C.a.main},f.a.createElement("h3",null,"\u6ce8\u518c"),f.a.createElement(d["a"],{onSubmit:this.handleSubmit},f.a.createElement(P,null,r("mail",{rules:[{required:!0,message:"\u8bf7\u8f93\u5165\u90ae\u7bb1\u5730\u5740\uff01"},{type:"email",message:"\u90ae\u7bb1\u5730\u5740\u683c\u5f0f\u9519\u8bef\uff01"}]})(f.a.createElement(h["a"],{size:"large",placeholder:"\u90ae\u7bb1"}))),f.a.createElement(P,{help:m},f.a.createElement(c["a"],{content:f.a.createElement("div",{style:{padding:"4px 0"}},D[this.getPasswordStatus()],this.renderPasswordProgress(),f.a.createElement("div",{style:{marginTop:10}},"\u8bf7\u81f3\u5c11\u8f93\u5165 6 \u4e2a\u5b57\u7b26\u3002\u8bf7\u4e0d\u8981\u4f7f\u7528\u5bb9\u6613\u88ab\u731c\u5230\u7684\u5bc6\u7801\u3002")),overlayStyle:{width:240},placement:"right",visible:g},r("password",{rules:[{validator:this.checkPassword}]})(f.a.createElement(h["a"],{size:"large",type:"password",placeholder:"\u81f3\u5c116\u4f4d\u5bc6\u7801\uff0c\u533a\u5206\u5927\u5c0f\u5199"})))),f.a.createElement(P,null,r("confirm",{rules:[{required:!0,message:"\u8bf7\u786e\u8ba4\u5bc6\u7801\uff01"},{validator:this.checkConfirm}]})(f.a.createElement(h["a"],{size:"large",type:"password",placeholder:"\u786e\u8ba4\u5bc6\u7801"}))),f.a.createElement(P,null,f.a.createElement(N,{compact:!0},f.a.createElement(u["a"],{size:"large",value:p,onChange:this.changePrefix,style:{width:"20%"}},f.a.createElement(x,{value:"86"},"+86"),f.a.createElement(x,{value:"87"},"+87")),r("mobile",{rules:[{required:!0,message:"\u8bf7\u8f93\u5165\u624b\u673a\u53f7\uff01"},{pattern:/^1\d{10}$/,message:"\u624b\u673a\u53f7\u683c\u5f0f\u9519\u8bef\uff01"}]})(f.a.createElement(h["a"],{size:"large",style:{width:"80%"},placeholder:"11\u4f4d\u624b\u673a\u53f7"})))),f.a.createElement(P,null,f.a.createElement(l["a"],{gutter:8},f.a.createElement(o["a"],{span:16},r("captcha",{rules:[{required:!0,message:"\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801\uff01"}]})(f.a.createElement(h["a"],{size:"large",placeholder:"\u9a8c\u8bc1\u7801"}))),f.a.createElement(o["a"],{span:8},f.a.createElement(n["a"],{size:"large",disabled:i,className:C.a.getCaptcha,onClick:this.onGetCaptcha},i?"".concat(i," s"):"\u83b7\u53d6\u9a8c\u8bc1\u7801")))),f.a.createElement(P,null,f.a.createElement(n["a"],{size:"large",loading:a,className:C.a.submit,type:"primary",htmlType:"submit"},"\u6ce8\u518c"),f.a.createElement(b.a,{className:C.a.login,to:"/User/Login"},"\u4f7f\u7528\u5df2\u6709\u8d26\u6237\u767b\u5f55"))))}})||i)||i)}}]);