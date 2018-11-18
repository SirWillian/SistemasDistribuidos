using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Newtonsoft.Json;
using RazorPagesMovie.Models;

namespace RazorPagesMovie.Pages
{
    public class VooBuyModel : PageModel
    {
        public string Message { get; set; }
        public PassagemAerea passagem { get ; set; }

        /**
         * Ao receber uma requisição POST, recupera a passagem dos dados da sessão,
         * preenche alguns campos em branco da passagem e tenta comprá-la.
         */
        public async Task OnPostAsync()
        {
            Message = "wirue sarue";
            Console.WriteLine("I'm not here");
            HttpClient httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Accept.Clear();
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/plain"));

            if(HttpContext.Session.Get("passagem")!=null)
            {
                passagem=JsonConvert.DeserializeObject<PassagemAerea>(HttpContext.Session.GetString("passagem"));
                passagem.companhiaIda=Request.Form["companhiaIda"];
                passagem.companhiaVolta=Request.Form["companhiaVolta"];

                HttpResponseMessage response = await httpClient.PostAsXmlAsync(Constants.serverPath+"/voo",passagem);
                if(response.IsSuccessStatusCode)
                    Message=await response.Content.ReadAsStringAsync();
                else
                    Message="Ocorreu um erro.";
            }
            httpClient.Dispose();
        }
    }
}
