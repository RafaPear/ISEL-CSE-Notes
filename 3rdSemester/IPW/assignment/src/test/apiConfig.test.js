import { expect } from 'chai'
import { apiConfig } from '../main/data/foccacia-fapi-data.mjs'

describe('apiConfig', () => {
    it('should have the correct baseUrl', () => {
        expect(apiConfig.baseUrl).to.equal("http://api.football-data.org/v4/")
    })

    it('should build competitions URL without competitionCode', () => {
        const url = apiConfig.buildURL('competitions')
        expect(url).to.equal("http://api.football-data.org/v4/competitions/")
    })
    
    it('should build competitions URL with competitionCode', () => {
        const url = apiConfig.buildURL('competitions', { competitionCode: '2166' })
        expect(url).to.equal("http://api.football-data.org/v4/competitions/2166/")
    })

    it('should build teams URL with competitionCode and year', () => {
        const url = apiConfig.buildURL('teams', { competitionCode: '2166', year: 2021 })
        expect(url).to.equal("http://api.football-data.org/v4/competitions/2166/teams?season=2021")
    })
    
    it('should throw an error if year is missing', () => {
        expect(() => apiConfig.buildURL('teams', { competitionCode: '2166' }))
            .to.throw()
    })

    it('should throw an error if competitionCode is missing for teams endpoint', () => {
        expect(() => apiConfig.buildURL('teams', { year: 2021 }))
            .to.throw()
    })
    
    it('should throw an error for unknown endpoint', () => {
        expect(() => apiConfig.buildURL('unknownEndpoint'))
            .to.throw()
    })
})